package com.wsh;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringDemo2 {
	public static void main(String[] args) {
		MyClassPathXmlApplicationContext applicationContext = new MyClassPathXmlApplicationContext("classpath:spring-config.xml");
		System.out.println(applicationContext.getBean("student"));
		//关闭容器对象，触发Bean的销毁
		applicationContext.close();
	}
}
