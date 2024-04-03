/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.context.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Delegate for AbstractApplicationContext's post-processor handling.
 *
 * @author Juergen Hoeller
 * @since 4.0
 */
final class PostProcessorRegistrationDelegate {

	private PostProcessorRegistrationDelegate() {
	}


	public static void invokeBeanFactoryPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

		// Invoke BeanDefinitionRegistryPostProcessors first, if any.

		// 记录已经处理过的BeanFactoryPostProcessor集合，无需重复执行
		Set<String> processedBeans = new HashSet<>();

		// 对BeanDefinitionRegistry类型的处理
		if (beanFactory instanceof BeanDefinitionRegistry) {
			// 强行把beanFactory转为BeanDefinitionRegistry
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
			// 存放普通的BeanFactoryPostProcessor
			List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();

			// 存放BeanDefinitionRegistryPostProcessor，BeanDefinitionRegistryPostProcessor继承了BeanFactoryPostProcessor
			List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();

			// 循环遍历硬编码方式注册的BeanFactoryPostProcessor后置处理器
			for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
				// 区分普通的BeanFactoryPostProcessor和BeanDefinitionRegistryPostProcessor，分别放入不同的集合中
				if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
					BeanDefinitionRegistryPostProcessor registryProcessor =
							(BeanDefinitionRegistryPostProcessor) postProcessor;

					// 如果是BeanDefinitionRegistryPostProcessor的话，直接执行BeanDefinitionRegistryPostProcessor接口的postProcessBeanDefinitionRegistry方法
					registryProcessor.postProcessBeanDefinitionRegistry(registry);
					// 添加到我们用于保存的BeanDefinitionRegistryPostProcessor的集合中
					registryProcessors.add(registryProcessor);
				}
				else {
					// 若没有实现BeanDefinitionRegistryPostProcessor接口，那么就是普通BeanFactoryPostProcessor,把当前的后置处理器加入到regularPostProcessors中
					regularPostProcessors.add(postProcessor);
				}
			}

			// Do not initialize FactoryBeans here: We need to leave all regular beans
			// uninitialized to let the bean factory post-processors apply to them!
			// Separate between BeanDefinitionRegistryPostProcessors that implement
			// PriorityOrdered, Ordered, and the rest.

			// 记录本次要执行的BeanDefinitionRegistryPostProcessor
			List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();

			// 配置注册的后置处理器
			/**
			 * 1、调用所有实现PriorityOrdered接口的BeanDefinitionRegistryPostProcessor实现类
			 */

			// 找出所有实现BeanDefinitionRegistryPostProcessor接口的Bean
			String[] postProcessorNames =
					beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			// 循环遍历，判断是否实现PriorityOrdered接口
			for (String ppName : postProcessorNames) {
				// 判断是否实现了PriorityOrdered接口
				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
					// 显示的调用getBean()的方式获取出该对象然后加入到currentRegistryProcessors集合中去
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));

					// 添加到将要执行的集合中，避免重复执行
					processedBeans.add(ppName);
				}
			}
			// 按照优先级进行排序
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			// 加入到用于保存到registryProcessors中
			registryProcessors.addAll(currentRegistryProcessors);

			/**
			 * 典型的BeanDefinitionRegistryPostProcessor就是ConfigurationClassPostProcessor
			 * 用于进行bean定义的加载 比如我们的包扫描，@import等
			 */
			// 调用BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry()方法
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			// 调用完之后，马上清空
			currentRegistryProcessors.clear();

			/**
			 * 2、调用所有实现了Ordered接口的BeanDefinitionRegistryPostProcessor实现类
			 */

			// 找出所有实现BeanDefinitionRegistryPostProcessor接口的类
			postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);

			// 循环遍历，判断是否实现Ordered接口
			for (String ppName : postProcessorNames) {
				// 未执行过 && 实现Ordered接口
				if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));

					// 添加到将要执行的集合中，避免重复执行
					processedBeans.add(ppName);
				}
			}
			// 按照order排序
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			registryProcessors.addAll(currentRegistryProcessors);

			// 调用BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry()方法
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			currentRegistryProcessors.clear();

			/**
			 * 3、调用所有剩下的没有实现任何优先级接口BeanDefinitionRegistryPostProcessors
			 */

			//定义一个重复处理的开关变量 默认值为true
			boolean reiterate = true;
			while (reiterate) {
				reiterate = false;
				// 去容器中获取BeanDefinitionRegistryPostProcessor的bean的处理器名称
				postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
				// 循环上一步获取的BeanDefinitionRegistryPostProcessor的类型名称
				for (String ppName : postProcessorNames) {
					// 未执行过的
					if (!processedBeans.contains(ppName)) {
						currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
						processedBeans.add(ppName);
						reiterate = true;
					}
				}
				// 排序
				sortPostProcessors(currentRegistryProcessors, beanFactory);
				registryProcessors.addAll(currentRegistryProcessors);

				// 调用BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry()方法
				invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
				currentRegistryProcessors.clear();
			}

			// 回调所有BeanDefinitionRegistryPostProcessor的postProcessBeanFactory方法
			invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);

			// 回调普通BeanFactoryPostProcessor的postProcessBeanFactory方法
			invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
		}

		else {
			// 调用在上下文实例中注册的工厂处理器
			invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
		}

		// Do not initialize FactoryBeans here: We need to leave all regular beans
		// uninitialized to let the bean factory post-processors apply to them!

		// 从bean工厂中获取到BeanFactoryPostProcessor
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

		// Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.

		// 存放实现了PriorityOrdered接口的BeanFactoryPostProcessor
		List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();

		// 存放实现了Ordered接口的BeanFactoryPostProcessor
		List<String> orderedPostProcessorNames = new ArrayList<>();

		// 存放其它BeanFactoryPostProcessor
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();

		// 循环从工厂中获取的BeanFactoryPostProcessor, 分别存入到三个不同的集合中
		for (String ppName : postProcessorNames) {
			// 针对已经处理过的BeanFactoryPostProcessor,不做任何操作，无需重复执行
			if (processedBeans.contains(ppName)) {
				// skip - already processed in first phase above
			}
			else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				// PriorityOrdered接口的BeanFactoryPostProcessor
				priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				// Ordered接口的BeanFactoryPostProcessor
				orderedPostProcessorNames.add(ppName);
			}
			else {
				// 普通BeanFactoryPostProcessor
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// 1、调用所有实现PriorityOrdered接口的BeanFactoryPostProcessor

		// 排序
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		// 执行postProcessBeanFactory()回调
		invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

		// 2、调用所有实现Ordered接口的BeanFactoryPostProcessor
		List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>();
		for (String postProcessorName : orderedPostProcessorNames) {
			// 这里会触发BeanFactoryPostProcessor的创建流程
			orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		// 排序
		sortPostProcessors(orderedPostProcessors, beanFactory);
		// 执行postProcessBeanFactory()回调
		invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);

		// 3、调用所有其他BeanFactoryPostProcessor
		List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>();
		for (String postProcessorName : nonOrderedPostProcessorNames) {
			nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		// 执行postProcessBeanFactory()回调
		invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);

		// Clear cached merged bean definitions since the post-processors might have
		// modified the original metadata, e.g. replacing placeholders in values...

		// 清除元数据缓存
		beanFactory.clearMetadataCache();
	}

	public static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {

		// 1、从bean工厂中获取到所有实现了BeanPostProcessor接口的bean
		// 如本例中我们自定义的MyBeanPostProcessor就会在这里被扫描出
		String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);

		// Register BeanPostProcessorChecker that logs an info message when
		// a bean is created during BeanPostProcessor instantiation, i.e. when
		// a bean is not eligible for getting processed by all BeanPostProcessors.
		int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;

		// 往bean工厂中添加一个BeanPostProcessor -> BeanPostProcessorChecker
		// BeanPostProcessorChecker是一个在创建bean期间记录信息消息的BeanPostProcessor
		beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));

		// 存放实现了PriorityOrdered接口的BeanPostProcessor【实现了PriorityOrdered接口的】
		List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();

		// 存放实现了MergedBeanDefinitionPostProcessor接口的BeanPostProcessor【容器内部的】
		List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();

		// 存放实现了Ordered接口的BeanPostProcessor【实现了Ordered接口的】
		List<String> orderedPostProcessorNames = new ArrayList<>();

		// 存放普通的BeanPostProcessor
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();

		// 2、循环遍历扫描出的所有的BeanPostProcessor，然后分别存放到前面定义的几个集合中
		for (String ppName : postProcessorNames) {

			// 实现了PriorityOrdered接口的BeanPostProcessor
			if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				// 通过getBean获取对应的Bean实例，然后添加到priorityOrderedPostProcessors集合中
				// 这里涉及到获取bean的过程，暂且不深入分析
				BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
				priorityOrderedPostProcessors.add(pp);

				// 如果同时实现了MergedBeanDefinitionPostProcessor接口，加入到internalPostProcessors集合中
				if (pp instanceof MergedBeanDefinitionPostProcessor) {
					internalPostProcessors.add(pp);
				}
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				// 实现了Ordered接口的BeanPostProcessor
				orderedPostProcessorNames.add(ppName);
			}
			else {
				// 普通BeanPostProcessor
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// 排序
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);

		// 3、注册实现了PriorityOrdered接口的BeanPostProcessor
		registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

		List<BeanPostProcessor> orderedPostProcessors = new ArrayList<>();
		for (String ppName : orderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			orderedPostProcessors.add(pp);

			// 如果同时实现了MergedBeanDefinitionPostProcessor接口，加入到internalPostProcessors集合中
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		// 排序
		sortPostProcessors(orderedPostProcessors, beanFactory);

		// 4、注册实现了Ordered接口的BeanPostProcessor
		// 通过beanFactory.addBeanPostProcessor(postProcessor)添加BeanPostProcessor
		registerBeanPostProcessors(beanFactory, orderedPostProcessors);

		// Now, register all regular BeanPostProcessors.

		// 5、注册其他普通的BeanPostProcessor
		List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<>();
		for (String ppName : nonOrderedPostProcessorNames) {
			BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
			nonOrderedPostProcessors.add(pp);

			// 如果同时实现了MergedBeanDefinitionPostProcessor接口，加入到internalPostProcessors集合中
			if (pp instanceof MergedBeanDefinitionPostProcessor) {
				internalPostProcessors.add(pp);
			}
		}
		// 通过beanFactory.addBeanPostProcessor(postProcessor)添加BeanPostProcessor
		registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

		// 排序
		sortPostProcessors(internalPostProcessors, beanFactory);

		// 通过beanFactory.addBeanPostProcessor(postProcessor)添加BeanPostProcessor
		// 6、注册所有内部 BeanPostProcessor
		registerBeanPostProcessors(beanFactory, internalPostProcessors);

		// Re-register post-processor for detecting inner beans as ApplicationListeners,
		// moving it to the end of the processor chain (for picking up proxies etc).

		// 7、往bean工厂中添加一个BeanPostProcessor -> ApplicationListenerDetector
		// ApplicationListenerDetector主要是检测bean是否实现了ApplicationListener接口，如果实现了的话，将会将bean注册到事件发布器applicationEventMulticaster中
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
	}

	private static void sortPostProcessors(List<?> postProcessors, ConfigurableListableBeanFactory beanFactory) {
		Comparator<Object> comparatorToUse = null;
		if (beanFactory instanceof DefaultListableBeanFactory) {
			comparatorToUse = ((DefaultListableBeanFactory) beanFactory).getDependencyComparator();
		}
		if (comparatorToUse == null) {
			comparatorToUse = OrderComparator.INSTANCE;
		}
		postProcessors.sort(comparatorToUse);
	}

	/**
	 * Invoke the given BeanDefinitionRegistryPostProcessor beans.
	 */
	private static void invokeBeanDefinitionRegistryPostProcessors(
			Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {

		for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
			postProcessor.postProcessBeanDefinitionRegistry(registry);
		}
	}

	/**
	 * Invoke the given BeanFactoryPostProcessor beans.
	 */
	private static void invokeBeanFactoryPostProcessors(
			Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {

		for (BeanFactoryPostProcessor postProcessor : postProcessors) {
			// 回调postProcessBeanFactory()方法
			postProcessor.postProcessBeanFactory(beanFactory);
		}
	}

	// 注册给定的BeanPostProcessor
	private static void registerBeanPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {

		// 遍历给定的BeanPostProcessor集合，挨个通过addBeanPostProcessor方法，向bean工厂中添加，保存到BeanFactory的beanPostProcessors成员变量中
		// private final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();
		for (BeanPostProcessor postProcessor : postProcessors) {
			beanFactory.addBeanPostProcessor(postProcessor);
		}
	}


	/**
	 * BeanPostProcessor that logs an info message when a bean is created during
	 * BeanPostProcessor instantiation, i.e. when a bean is not eligible for
	 * getting processed by all BeanPostProcessors.
	 */
	private static final class BeanPostProcessorChecker implements BeanPostProcessor {

		private static final Log logger = LogFactory.getLog(BeanPostProcessorChecker.class);

		private final ConfigurableListableBeanFactory beanFactory;

		private final int beanPostProcessorTargetCount;

		public BeanPostProcessorChecker(ConfigurableListableBeanFactory beanFactory, int beanPostProcessorTargetCount) {
			this.beanFactory = beanFactory;
			this.beanPostProcessorTargetCount = beanPostProcessorTargetCount;
		}

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) {
			return bean;
		}

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) {
			if (!(bean instanceof BeanPostProcessor) && !isInfrastructureBean(beanName) &&
					this.beanFactory.getBeanPostProcessorCount() < this.beanPostProcessorTargetCount) {
				if (logger.isInfoEnabled()) {
					logger.info("Bean '" + beanName + "' of type [" + bean.getClass().getName() +
							"] is not eligible for getting processed by all BeanPostProcessors " +
							"(for example: not eligible for auto-proxying)");
				}
			}
			return bean;
		}

		private boolean isInfrastructureBean(@Nullable String beanName) {
			if (beanName != null && this.beanFactory.containsBeanDefinition(beanName)) {
				BeanDefinition bd = this.beanFactory.getBeanDefinition(beanName);
				return (bd.getRole() == RootBeanDefinition.ROLE_INFRASTRUCTURE);
			}
			return false;
		}
	}

}
