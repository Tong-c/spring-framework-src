package com.wsh.beanpostprocessor;


import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
		System.out.println( applicationContext.getBean("student"));
	}
}
