package com.wsh;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("执行MyBeanPostProcessor的postProcessBeforeInitialization()方法, beanName=" + beanName);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("执行MyBeanPostProcessor的postProcessAfterInitialization()方法, beanName=" + beanName);
		return bean;
	}

}
