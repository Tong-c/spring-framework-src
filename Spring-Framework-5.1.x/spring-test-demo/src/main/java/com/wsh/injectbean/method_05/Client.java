package com.wsh.injectbean.method_05;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 第五种方式：@Import方式
 */
public class Client {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		for (String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
			System.out.println(beanDefinitionName);
		}
		System.out.println("================");
		System.out.println(applicationContext.getBean("com.wsh.injectbean.method_05.Student"));
		System.out.println(applicationContext.getBean("student"));
	}
}
