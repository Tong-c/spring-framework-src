package com.tc.beans;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractB {
	@Autowired
	private A a;

	public void print() {
		a.print();
	}
}
