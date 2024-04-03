package com.wsh.listener.observer;

public class Test {
	public static void main(String[] args) {
		ConcreteSubject concreteSubject = new ConcreteSubject();
		concreteSubject.addObserver(new ConcreteObserver());
		// 被观察者发出消息
		concreteSubject.sendMsg("hello world!");
	}
}
