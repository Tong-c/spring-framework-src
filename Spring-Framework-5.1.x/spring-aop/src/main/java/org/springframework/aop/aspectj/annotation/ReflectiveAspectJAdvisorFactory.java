/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.aop.aspectj.annotation;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.aopalliance.aop.Advice;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.aspectj.lang.annotation.Pointcut;

import org.springframework.aop.Advisor;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.aspectj.AbstractAspectJAdvice;
import org.springframework.aop.aspectj.AspectJAfterAdvice;
import org.springframework.aop.aspectj.AspectJAfterReturningAdvice;
import org.springframework.aop.aspectj.AspectJAfterThrowingAdvice;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.DeclareParentsAdvisor;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConvertingComparator;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.comparator.InstanceComparator;

/**
 * Factory that can create Spring AOP Advisors given AspectJ classes from
 * classes honoring the AspectJ 5 annotation syntax, using reflection to
 * invoke the corresponding advice methods.
 *
 * @author Rod Johnson
 * @author Adrian Colyer
 * @author Juergen Hoeller
 * @author Ramnivas Laddad
 * @author Phillip Webb
 * @since 2.0
 */
@SuppressWarnings("serial")
public class ReflectiveAspectJAdvisorFactory extends AbstractAspectJAdvisorFactory implements Serializable {

	private static final Comparator<Method> METHOD_COMPARATOR;

	static {
		Comparator<Method> adviceKindComparator = new ConvertingComparator<>(
				new InstanceComparator<>(
						Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class),
				(Converter<Method, Annotation>) method -> {
					AspectJAnnotation<?> ann = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(method);
					return (ann != null ? ann.getAnnotation() : null);
				});
		Comparator<Method> methodNameComparator = new ConvertingComparator<>(Method::getName);
		METHOD_COMPARATOR = adviceKindComparator.thenComparing(methodNameComparator);
	}


	@Nullable
	private final BeanFactory beanFactory;


	/**
	 * Create a new {@code ReflectiveAspectJAdvisorFactory}.
	 */
	public ReflectiveAspectJAdvisorFactory() {
		this(null);
	}

	/**
	 * Create a new {@code ReflectiveAspectJAdvisorFactory}, propagating the given
	 * {@link BeanFactory} to the created {@link AspectJExpressionPointcut} instances,
	 * for bean pointcut handling as well as consistent {@link ClassLoader} resolution.
	 * @param beanFactory the BeanFactory to propagate (may be {@code null}}
	 * @since 4.3.6
	 * @see AspectJExpressionPointcut#setBeanFactory
	 * @see org.springframework.beans.factory.config.ConfigurableBeanFactory#getBeanClassLoader()
	 */
	public ReflectiveAspectJAdvisorFactory(@Nullable BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}


	@Override
	// org.springframework.aop.aspectj.annotation.ReflectiveAspectJAdvisorFactory.getAdvisors
	public List<Advisor> getAdvisors(MetadataAwareAspectInstanceFactory aspectInstanceFactory) {
		// 获取到当前处理的切面类类型
		Class<?> aspectClass = aspectInstanceFactory.getAspectMetadata().getAspectClass();
		// 获取到当前处理的切面类名称
		String aspectName = aspectInstanceFactory.getAspectMetadata().getAspectName();
		// 对切面类进行一些校验
		validate(aspectClass);

		// We need to wrap the MetadataAwareAspectInstanceFactory with a decorator so that it will only instantiate once.
		// 包装MetadataAwareAspectInstanceFactory, 保证只会实例化一次
		MetadataAwareAspectInstanceFactory lazySingletonAspectInstanceFactory =
				new LazySingletonAspectInstanceFactoryDecorator(aspectInstanceFactory);

		// 存放增强器的集合
		List<Advisor> advisors = new ArrayList<>();

		// 获取切面类中的所有通知方法，也就是被@Around、@After等注解修饰的方法，但是会跳过@Pointcut修饰的通知方法
		for (Method method : getAdvisorMethods(aspectClass)) {
			// 循环解析切面中的通知方法，将通知方法包装成Advisor增强器
			Advisor advisor = getAdvisor(method, lazySingletonAspectInstanceFactory, advisors.size(), aspectName);
			if (advisor != null) {
				// 如果增强器不为空，则添加到advisors集合中
				advisors.add(advisor);
			}
		}

		// If it's a per target aspect, emit the dummy instantiating aspect.
		// 如果增强器advisors不为空，而且配置了增强延迟初始化，那么需要在首位加入同步实例化增强器（保证增强使用之前的实例化）
		if (!advisors.isEmpty() && lazySingletonAspectInstanceFactory.getAspectMetadata().isLazilyInstantiated()) {
			Advisor instantiationAdvisor = new SyntheticInstantiationAdvisor(lazySingletonAspectInstanceFactory);
			// 添加到集合中第一个位置
			advisors.add(0, instantiationAdvisor);
		}

		// 获取DeclareParents注解(引介增强)
		for (Field field : aspectClass.getDeclaredFields()) {
			Advisor advisor = getDeclareParentsAdvisor(field);
			if (advisor != null) {
				advisors.add(advisor);
			}
		}

		return advisors;
	}

	// org.springframework.aop.aspectj.annotation.ReflectiveAspectJAdvisorFactory.getAdvisorMethods
	private List<Method> getAdvisorMethods(Class<?> aspectClass) {
		// 1.获取到当前切面类aspectClass的所有通知方法
		final List<Method> methods = new ArrayList<>();

		ReflectionUtils.doWithMethods(aspectClass, method -> {
			// 2.排除掉@Pointcut注解修饰的方法
			if (AnnotationUtils.getAnnotation(method, Pointcut.class) == null) {
				methods.add(method);
			}
		}, ReflectionUtils.USER_DECLARED_METHODS);

		// 3.对通知方法进行排序
		if (methods.size() > 1) {
			methods.sort(METHOD_COMPARATOR);
		}

		// 4.返回通知方法
		return methods;
	}

	/**
	 * Build a {@link org.springframework.aop.aspectj.DeclareParentsAdvisor}
	 * for the given introduction field.
	 * <p>Resulting Advisors will need to be evaluated for targets.
	 * @param introductionField the field to introspect
	 * @return the Advisor instance, or {@code null} if not an Advisor
	 */
	@Nullable
	private Advisor getDeclareParentsAdvisor(Field introductionField) {
		DeclareParents declareParents = introductionField.getAnnotation(DeclareParents.class);
		if (declareParents == null) {
			// Not an introduction field
			return null;
		}

		if (DeclareParents.class == declareParents.defaultImpl()) {
			throw new IllegalStateException("'defaultImpl' attribute must be set on DeclareParents");
		}

		return new DeclareParentsAdvisor(
				introductionField.getType(), declareParents.value(), declareParents.defaultImpl());
	}


	@Override
	@Nullable
	// 将通知方法包装成Advisor增强器
	// org.springframework.aop.aspectj.annotation.ReflectiveAspectJAdvisorFactory.getAdvisor
	public Advisor getAdvisor(Method candidateAdviceMethod, MetadataAwareAspectInstanceFactory aspectInstanceFactory,
			int declarationOrderInAspect, String aspectName) {

		// 对切面类进行一些校验
		validate(aspectInstanceFactory.getAspectMetadata().getAspectClass());

		// 获取切点信息，封装成AspectJExpressionPointcut
		AspectJExpressionPointcut expressionPointcut = getPointcut(candidateAdviceMethod, aspectInstanceFactory.getAspectMetadata().getAspectClass());
		if (expressionPointcut == null) {
			return null;
		}

		// 根据切点信息生成增强器，用于创建切面通知对象
		return new InstantiationModelAwarePointcutAdvisorImpl(expressionPointcut, candidateAdviceMethod, this, aspectInstanceFactory, declarationOrderInAspect, aspectName);
	}

	@Nullable
	// 指定注解的表达式信息的获取
	private AspectJExpressionPointcut getPointcut(Method candidateAdviceMethod, Class<?> candidateAspectClass) {
		// 查找并返回给定方法的第一个AspectJ注解
		AspectJAnnotation<?> aspectJAnnotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(candidateAdviceMethod);

		// 如果方法没有使用AspectJ的注解，则返回null
		if (aspectJAnnotation == null) {
			return null;
		}

		// 封装AspectJExpressionPointcut，并提取得到@PointCut注解中的切入点表达式
		AspectJExpressionPointcut ajexp = new AspectJExpressionPointcut(candidateAspectClass, new String[0], new Class<?>[0]);
		ajexp.setExpression(aspectJAnnotation.getPointcutExpression());
		if (this.beanFactory != null) {
			ajexp.setBeanFactory(this.beanFactory);
		}
		return ajexp;
	}


	@Override
	@Nullable
	// org.springframework.aop.aspectj.annotation.ReflectiveAspectJAdvisorFactory.getAdvice
	public Advice getAdvice(Method candidateAdviceMethod, AspectJExpressionPointcut expressionPointcut,
			MetadataAwareAspectInstanceFactory aspectInstanceFactory, int declarationOrder, String aspectName) {

		// 获取切面类类型
		Class<?> candidateAspectClass = aspectInstanceFactory.getAspectMetadata().getAspectClass();
		// 校验切面类
		validate(candidateAspectClass);

		// 查找并返回方法的第一个aspectJ注解
		AspectJAnnotation<?> aspectJAnnotation =
				AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(candidateAdviceMethod);
		// 判断解析出来的注解信息是否为null
		if (aspectJAnnotation == null) {
			return null;
		}

		// If we get here, we know we have an AspectJ method.
		// Check that it's an AspectJ-annotated class

		// 再次校验切面类是否使用了@AspectJ注解
		if (!isAspect(candidateAspectClass)) {
			throw new AopConfigException("Advice must be declared inside an aspect type: " +
					"Offending method '" + candidateAdviceMethod + "' in class [" +
					candidateAspectClass.getName() + "]");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Found AspectJ method: " + candidateAdviceMethod);
		}

		AbstractAspectJAdvice springAdvice;

		// 根据方法使用的aspectJ注解创建对应的增强器
		switch (aspectJAnnotation.getAnnotationType()) {
			// 如果是PointCut注解，那么就抛出异常
			case AtPointcut:
				if (logger.isDebugEnabled()) {
					logger.debug("Processing pointcut '" + candidateAdviceMethod.getName() + "'");
				}
				return null;
			// 如果是@Around环绕通知,则构建AspectJAroundAdvice通知
			case AtAround:
				springAdvice = new AspectJAroundAdvice(
						candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
				break;
			// 如果是@Before前置通知,则构建AspectJMethodBeforeAdvice通知
			case AtBefore:
				springAdvice = new AspectJMethodBeforeAdvice(
						candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
				break;
			// 如果是@After后置通知,则构建AspectJAfterAdvice通知
			case AtAfter:
				springAdvice = new AspectJAfterAdvice(
						candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
				break;
			// 如果是@AfterReturning返回通知,则构建AspectJAfterReturningAdvice通知
			case AtAfterReturning:
				springAdvice = new AspectJAfterReturningAdvice(
						candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
				AfterReturning afterReturningAnnotation = (AfterReturning) aspectJAnnotation.getAnnotation();
				if (StringUtils.hasText(afterReturningAnnotation.returning())) {
					springAdvice.setReturningName(afterReturningAnnotation.returning());
				}
				break;
			// 如果是@AfterThrowing异常通知,则构建AspectJAfterThrowingAdvice通知
			case AtAfterThrowing:
				springAdvice = new AspectJAfterThrowingAdvice(
						candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
				AfterThrowing afterThrowingAnnotation = (AfterThrowing) aspectJAnnotation.getAnnotation();
				if (StringUtils.hasText(afterThrowingAnnotation.throwing())) {
					springAdvice.setThrowingName(afterThrowingAnnotation.throwing());
				}
				break;
			default:
				throw new UnsupportedOperationException(
						"Unsupported advice type on method: " + candidateAdviceMethod);
		}

		// 配置通知对象的相关属性，如设置切面名称、参数等
		springAdvice.setAspectName(aspectName);
		springAdvice.setDeclarationOrder(declarationOrder);
		String[] argNames = this.parameterNameDiscoverer.getParameterNames(candidateAdviceMethod);
		if (argNames != null) {
			springAdvice.setArgumentNamesFromStringArray(argNames);
		}
		springAdvice.calculateArgumentBindings();

		return springAdvice;
	}


	/**
	 * Synthetic advisor that instantiates the aspect.
	 * Triggered by per-clause pointcut on non-singleton aspect.
	 * The advice has no effect.
	 */
	@SuppressWarnings("serial")
	protected static class SyntheticInstantiationAdvisor extends DefaultPointcutAdvisor {

		public SyntheticInstantiationAdvisor(final MetadataAwareAspectInstanceFactory aif) {
			super(aif.getAspectMetadata().getPerClausePointcut(), (MethodBeforeAdvice)
					(method, args, target) -> aif.getAspectInstance());
		}
	}

}
