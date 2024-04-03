package com.wsh.configuration;

public class Student {
	private Teacher teacher;

	private String name;

	public Student(Teacher teacher) {
		this.teacher = teacher;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Student{" +
				"teacher=" + teacher +
				", name='" + name + '\'' +
				'}';
	}
}

