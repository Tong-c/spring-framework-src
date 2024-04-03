package com.wsh.importselector;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Clinet {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		Object student = annotationConfigApplicationContext.getBean("student");
		System.out.println("student = " + student);
	}
}
