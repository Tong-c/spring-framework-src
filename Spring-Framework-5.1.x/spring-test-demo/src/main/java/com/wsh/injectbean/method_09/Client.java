package com.wsh.injectbean.method_09;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 第九种方式：BeanFactoryPostProcessor方式
 * <p>
 * 1. BeanDefinitionRegistryPostProcessor: 侧重于bean的注册;
 * 2. BeanFactoryPostProcessor:侧重于对已经注册的bean的属性进行修改，虽然也可以注册bean;
 */
public class Client {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		System.out.println(applicationContext.getBean("product"));
	}
}
