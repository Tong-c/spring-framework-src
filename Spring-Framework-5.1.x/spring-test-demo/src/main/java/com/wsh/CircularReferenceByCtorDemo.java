package com.wsh;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CircularReferenceByCtorDemo {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-cons-config.xml");
		System.out.println(applicationContext.getBean("a"));
		//关闭容器对象，触发Bean的销毁
		applicationContext.close();
	}
}
