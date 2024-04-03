package com.wsh.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan("com.wsh.configuration")
public class MyBeanConfig {

//	@Autowired
//	private Teacher teacher;

	@Bean
	public Teacher teacher() {
		Teacher teacher = new Teacher();
		System.out.println("teacher()方法返回的teacher对象：" + teacher.hashCode());
		return teacher;
	}

	@Bean
	public Student student() {
		Teacher teacher2 = teacher();
		System.out.println("student()方法获取的teacher对象：" + teacher2.hashCode());
//		System.out.println("@Autowired注入的teacher对象：" + teacher.hashCode());
		return new Student(teacher2);
	}

}
