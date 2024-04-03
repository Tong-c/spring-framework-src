package com.wsh.beanfactorypostprocessor;

import java.io.Serializable;

public class Student implements Serializable {

	private static final long serialVersionUID = 6682300509742778346L;
	private String id;

	private String name;

	private Integer age;

	public Student() {
		System.out.println("Student实例化...");
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

	@Override
	public String toString() {
		return "Student{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}
