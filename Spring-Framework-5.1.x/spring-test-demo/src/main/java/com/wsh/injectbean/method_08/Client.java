package com.wsh.injectbean.method_08;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 第八种方式：BeanDefinitionRegistryPostProcessor方式
 */
public class Client {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		System.out.println(applicationContext.getBean("user"));
	}
}
