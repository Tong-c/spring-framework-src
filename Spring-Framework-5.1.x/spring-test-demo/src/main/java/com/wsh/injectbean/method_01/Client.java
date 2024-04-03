package com.wsh.injectbean.method_01;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 第一种方式： XML文件配置
 */
public class Client {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
		System.out.println(applicationContext.getBean("student"));
	}
}
