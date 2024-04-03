package com.wsh.aop;

import com.wsh.aop.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Client {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AopConfig.class);
		System.out.println(annotationConfigApplicationContext.getBean(UserService.class));
//		SimpleController simpleController = (SimpleController) annotationConfigApplicationContext.getBean("simpleController");
//		simpleController.test();
	}

}
