package com.tc.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

	@Bean
	public B b() {
		return new B(a());
	}

	@Bean
	public A a() {
		return new A();
	}
}
