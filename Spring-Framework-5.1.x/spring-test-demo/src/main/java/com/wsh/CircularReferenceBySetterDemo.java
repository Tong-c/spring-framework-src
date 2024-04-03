package com.wsh;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CircularReferenceBySetterDemo {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-setter-config.xml");
		System.out.println(applicationContext.getBean("a"));
		System.out.println(applicationContext.getBean("b"));
		//关闭容器对象，触发Bean的销毁
		applicationContext.close();
	}
}
