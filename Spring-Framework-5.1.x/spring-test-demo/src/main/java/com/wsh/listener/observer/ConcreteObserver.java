package com.wsh.listener.observer;

/**
 * 具体观察者角色
 */
public class ConcreteObserver extends AbstractObserver {

	private String context;

	public String getContext() {
		return context;
	}

	@Override
	public void receiveMsg(String context) {
		this.context = context;
		System.out.println("具体观察者角色接收消息: " + context);
	}
}
