package com.wsh.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	// 声明式事务
	@Transactional
	public void insert() {
		userDao.insert();
		System.out.println("插入完成");
//		int value = 1 / 0;
	}

}
