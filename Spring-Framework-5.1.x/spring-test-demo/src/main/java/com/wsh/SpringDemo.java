package com.wsh;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringDemo {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
		System.out.println(applicationContext.getBean("student"));
		//关闭容器对象，触发Bean的销毁
		applicationContext.close();
	}
}
