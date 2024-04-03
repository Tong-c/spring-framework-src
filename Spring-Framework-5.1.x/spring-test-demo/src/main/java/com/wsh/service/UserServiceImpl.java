package com.wsh.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

	@Override
	public void addUser() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("新增用户...");
	}
}
