package com.wsh.aopdemo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("userService")
public class UserService {

	@Autowired
	private OrderService orderService;

	public void addUser() {
		orderService.addOrder();
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		int i = 1 / 0;
		System.out.println("add user...orderService = " + orderService);
	}
}
