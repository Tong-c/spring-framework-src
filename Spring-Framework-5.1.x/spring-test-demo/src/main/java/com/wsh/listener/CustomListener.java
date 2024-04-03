package com.wsh.listener;

import org.springframework.context.ApplicationListener;

/**
 * 自定义事件监听器
 */
public class CustomListener implements ApplicationListener<CustomEvent> {
	@Override
	public void onApplicationEvent(CustomEvent event) {
		// 监听事件，处理具体逻辑
		System.out.println("event.getContext() = " + event.getContext());
	}
}
