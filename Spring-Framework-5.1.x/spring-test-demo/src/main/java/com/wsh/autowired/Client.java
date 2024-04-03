package com.wsh.autowired;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Client {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = (UserService) annotationConfigApplicationContext.getBean("userServiceImpl");
		userService.insert();
	}
}