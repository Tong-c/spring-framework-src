package com.tc;

import com.tc.beans.B;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanFactoryTest {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.tc");
		context.refresh();
		B b = (B) context.getBean("b");
		b.print();
	}
}
