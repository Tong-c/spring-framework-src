package com.wsh;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {
	public MyClassPathXmlApplicationContext(String... configLocations) throws BeansException {
		super(configLocations);
	}

	@Override
	protected void initPropertySources() {
		getEnvironment().setRequiredProperties("WSH");
	}

	@Override
	protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
		// 不允许覆盖同名称的不同定义的对象
		super.setAllowBeanDefinitionOverriding(false);
		// 不允许存在循环依赖
		super.setAllowCircularReferences(false);
		super.customizeBeanFactory(beanFactory);
	}
}
