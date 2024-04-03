package com.wsh.circularreference;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CircularReferenceDemo {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
		System.out.println(applicationContext.getBean("a"));
	}
}
