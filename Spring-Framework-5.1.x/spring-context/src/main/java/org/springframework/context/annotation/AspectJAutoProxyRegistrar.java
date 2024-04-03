/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.context.annotation;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Registers an {@link org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator
 * AnnotationAwareAspectJAutoProxyCreator} against the current {@link BeanDefinitionRegistry}
 * as appropriate based on a given @{@link EnableAspectJAutoProxy} annotation.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.1
 * @see EnableAspectJAutoProxy
 */
class AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar {

	/**
	 * Register, escalate, and configure the AspectJ auto proxy creator based on the value
	 * of the @{@link EnableAspectJAutoProxy#proxyTargetClass()} attribute on the importing
	 * {@code @Configuration} class.
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

		// 1.向IOC容器中注入AnnotationAwareAspectJAutoProxyCreator组件的bean定义信息（基于注解的自动代理创建器）, 并且beanName = "org.springframework.aop.config.internalAutoProxyCreator"
		AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);

		// 2.获取@EnableAspectJAutoProxy注解的属性信息
		AnnotationAttributes enableAspectJAutoProxy =
				AnnotationConfigUtils.attributesFor(importingClassMetadata, EnableAspectJAutoProxy.class);
		if (enableAspectJAutoProxy != null) {
			// 3.设置使用Cglib动态代理
			if (enableAspectJAutoProxy.getBoolean("proxyTargetClass")) {
				// 强制使用CGLIB动态代理,实际上是设置beanName = "org.springframework.aop.config.internalAutoProxyCreator"这个bean的proxyTargetClass属性值为true
				AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
			}

			// 4.设置代理的暴露方式，即强制暴露Bean的代理对象到AopContext中
			if (enableAspectJAutoProxy.getBoolean("exposeProxy")) {
				// 实际上是设置beanName = "org.springframework.aop.config.internalAutoProxyCreator"这个bean的exposeProxy属性值为true
				AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
			}
		}
	}

}