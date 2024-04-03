package com.wsh.condition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

// 只有当CommonOpenCondition类中的matches()方法返回true时，才会注册这个类注册的所有Bean
@Conditional({CommonOpenCondition.class})
@Configuration
public class AppConfig {

	@Bean
	public Student student() {
		return new Student();
	}

	@Bean
	public Teacher teacher() {
		return new Teacher();
	}

}
