package com.wsh.beanpostprocessor;

import org.springframework.beans.factory.InitializingBean;

import java.io.Serializable;

public class Student implements Serializable, InitializingBean {

	private static final long serialVersionUID = -5150709193154459622L;
	private String id;

	private String name;

	private Integer age;

	public Student() {
		System.out.println("执行Student实例化...");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public void init() {
		System.out.println("执行Student的init()方法...");
	}

	public void destroy() {
		System.out.println("执行Student的destroy()方法...");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("执行Student的afterPropertiesSet()方法...");
	}

	@Override
	public String toString() {
		return "Student{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}
