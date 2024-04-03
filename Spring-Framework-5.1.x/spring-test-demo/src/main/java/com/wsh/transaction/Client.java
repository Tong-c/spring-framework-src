package com.wsh.transaction;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Client {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(TxConfig.class);
		UserService userService = (UserService) annotationConfigApplicationContext.getBean("userServiceImpl");
		System.out.println(userService);
		userService.insert();
	}

}
