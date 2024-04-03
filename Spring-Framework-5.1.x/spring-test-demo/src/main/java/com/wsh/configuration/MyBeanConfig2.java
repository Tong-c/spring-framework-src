//package com.wsh.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MyBeanConfig2 {
//
//	@Bean
//	public Teacher teacher() {
//		Teacher teacher = new Teacher();
//		System.out.println("teacher()方法返回的teacher对象：" + teacher.hashCode());
//		return teacher;
//	}
//
//	@Bean
//	public Student student() {
//		System.out.println("========================================");
//		Teacher teacher = teacher();
//		System.out.println("student()方法获取的teacher对象：" + teacher.hashCode());
//		return new Student(teacher);
//	}
//
//}
