package com.wsh.configuration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Client {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(MyBeanConfig.class);
		Object student = annotationConfigApplicationContext.getBean("student");
		System.out.println("student = " + student);
	}
}
