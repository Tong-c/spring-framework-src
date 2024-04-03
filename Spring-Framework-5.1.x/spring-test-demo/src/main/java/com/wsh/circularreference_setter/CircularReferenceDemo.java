package com.wsh.circularreference_setter;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CircularReferenceDemo {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-setter-config.xml");
		System.out.println(applicationContext.getBean("a"));
	}
}
