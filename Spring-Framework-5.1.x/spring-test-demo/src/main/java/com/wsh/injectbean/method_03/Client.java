package com.wsh.injectbean.method_03;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 第三种方式：@Configuration + @Bean方式
 * <p>
 * 通常情况下，如果项目中有使用到第三方类库中的工具类的话，我们都是采用这种方式注册Bean
 */
public class Client {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		System.out.println(applicationContext.getBean("student"));
	}
}
