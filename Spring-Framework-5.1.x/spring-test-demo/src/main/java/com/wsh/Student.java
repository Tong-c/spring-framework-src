package com.wsh;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Student implements BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean {

	private ApplicationContext applicationContext;
	private BeanFactory beanFactory;

	public Student() {
		System.out.println("===========================instantiation===========================");
		System.out.println("execute student construction method instantiate...");
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void initMethod() {
		System.out.println("exec student custom init-method...");
	}

	public void destroyMethod() {
		System.out.println("exec student custom destroy method...");
	}

	@Override
	public String toString() {
		System.out.println("applicationContext= " + applicationContext);
		System.out.println("beanFactory= " + beanFactory);
		return "Student{" + "name='" + name + '\'' + '}';
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("exec ApplicationContextAware#setApplicationContext()...");
		this.applicationContext = applicationContext;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("===========================initialization===========================");
		System.out.println("exec BeanFactoryAware#setBeanFactory()...");
		this.beanFactory = beanFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("exec Student#InitializingBean#afterPropertiesSet()...");
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("===========================destroy===========================");
		System.out.println("exec DisposableBean#destroy()...");
	}

}
