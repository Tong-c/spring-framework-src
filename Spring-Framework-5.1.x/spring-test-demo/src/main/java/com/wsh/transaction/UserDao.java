package com.wsh.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void insert() {
		String sql = "INSERT INTO user(name, age) VALUES(?,?)";
		jdbcTemplate.update(sql, "lisi", 20);
	}

}
