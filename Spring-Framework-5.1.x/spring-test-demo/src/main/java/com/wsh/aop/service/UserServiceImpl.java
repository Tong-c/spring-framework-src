package com.wsh.aop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private OrderService orderService;

	@Override
	public void addUser() {
		orderService.addOrder();
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		int i = 1 / 0;
		System.out.println("新增用户...orderService = " + orderService);
	}
}
