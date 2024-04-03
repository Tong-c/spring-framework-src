package com.wsh.circularreference_constructor;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CircularReferenceDemo {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-cons-config.xml");
		System.out.println(applicationContext.getBean("a"));
	}
}
