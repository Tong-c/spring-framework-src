package com.wsh.listener;


import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
		// 直接使用applicationContext发布自定义事件
		applicationContext.publishEvent(new CustomEvent("customEvent", "hello world"));
		Thread.sleep(2000);
		applicationContext.publishEvent(new CustomEvent("customEvent", "hello, spring listener!!"));
	}
}
