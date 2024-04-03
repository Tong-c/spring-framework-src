package com.wsh.injectbean.method_04;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 第四种方式：使用FactoryBean
 *
 * FactoryBean在一些框架整合上用的比较多，如Mybatis与Spring的整合中：MapperFactoryBean、SqlSessionFactoryBean等
 */
public class Client {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		System.out.println(applicationContext.getBean("userFactoryBean"));
		System.out.println(applicationContext.getBean("&userFactoryBean"));
	}
}
