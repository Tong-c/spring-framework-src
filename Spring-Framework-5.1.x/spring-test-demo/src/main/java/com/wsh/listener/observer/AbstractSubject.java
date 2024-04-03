package com.wsh.listener.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象主题(抽象被观察者角色)
 */
public abstract class AbstractSubject {

	/**
	 * 持有所有抽象观察者角色的集合引用
	 */
	private List<AbstractObserver> observerList = new ArrayList<>();

	/**
	 * 添加一个观察者
	 *
	 * @param observer
	 */
	public void addObserver(AbstractObserver observer) {
		observerList.add(observer);
	}

	/**
	 * 移除一个观察者
	 *
	 * @param observer
	 */
	public void removeObserver(AbstractObserver observer) {
		observerList.remove(observer);
	}

	/**
	 * 通知所有的观察者，执行观察者更新方法
	 */
	public void notifyAllObserver(String context) {
		observerList.forEach(observer -> observer.receiveMsg(context));
	}

}
