package com.wsh.listener;

import org.springframework.context.ApplicationEvent;

/**
 * 自定义事件
 */
public class CustomEvent extends ApplicationEvent {
	private static final long serialVersionUID = -7423911586992107394L;

	/**
	 * Create a new ApplicationEvent.
	 *
	 * @param source the object on which the event initially occurred (never {@code null})
	 */
	public CustomEvent(Object source) {
		super(source);
	}

	private String context;

	public CustomEvent(Object source, String context) {
		super(source);
		this.context = context;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
}
