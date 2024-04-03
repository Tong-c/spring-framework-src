package com.wsh.beanfactorypostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

/**
 * @Description: 自定义BeanFactoryPostProcessor
 * @Date: 2021/12/23 11:16
 * 说明: BeanFactoryPostProcessor在容器实例化任何bean之前读取bean的定义(配置元数据)，并可以修改它
 * 可以通过实现Ordered接口指定order属性的值，从而设置BeanFactoryPostProcessor的执行顺序，order属性的值越小，优先级越高
 */
public class MyBeanFactoryPostProcessor02 implements BeanFactoryPostProcessor, Ordered {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("执行MyBeanFactoryPostProcessor02的postProcessBeanFactory()方法");

		// 获取工厂当前所有注册的beanNames数组
		String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();

		// 循环遍历
		for (String beanName : beanDefinitionNames) {
			if ("student".equals(beanName)) {
				// 根据beanName获取对应的bean定义信息
				// 当我们拿到BeanDefinition对象后，我们可以手动修改bean标签中所定义的属性值
				BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
				// 获取对应的属性值
				MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
				// 更新属性值
				if (propertyValues.contains("age")) {
					System.out.println("修改age属性...旧值:" + propertyValues.get("age"));
					propertyValues.add("age", "20");
				}
			}
		}
	}

	@Override
	public int getOrder() {
		return 1;
	}
}
