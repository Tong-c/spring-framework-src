package com.wsh.aopdemo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Client {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AopConfig.class);
		Object userService = annotationConfigApplicationContext.getBean("userService");
		System.out.println(userService);
		Object orderService = annotationConfigApplicationContext.getBean("orderService");
		System.out.println(orderService);
	}
}
