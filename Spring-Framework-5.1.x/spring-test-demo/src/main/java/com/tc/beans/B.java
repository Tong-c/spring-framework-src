package com.tc.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class B {

	private A a;

	public B(A a) {
		this.a = a;
	}

	public void print() {
		a.print();
	}
}
