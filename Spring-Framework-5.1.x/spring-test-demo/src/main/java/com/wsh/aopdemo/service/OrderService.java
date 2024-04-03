package com.wsh.aopdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orderService")
public class OrderService {

	@Autowired
	private UserService userService;

	public void addOrder() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("addOrder..., userService = " + userService);
	}
}
