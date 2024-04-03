package com.wsh.injectbean.method_03;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public Student student() {
		return new Student();
	}

}
