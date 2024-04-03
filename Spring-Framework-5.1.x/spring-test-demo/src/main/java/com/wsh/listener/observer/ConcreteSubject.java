package com.wsh.listener.observer;

/**
 * 具体被观察者角色
 */
public class ConcreteSubject extends AbstractSubject {
	private String context;

	public String getContext() {
		return context;
	}

	public void sendMsg(String context) {
		this.context = context;
		System.out.println("具体被观察者角色发送消息: " + context);
		// 通知到所有观察者角色，更新
		super.notifyAllObserver(context);
	}

}
