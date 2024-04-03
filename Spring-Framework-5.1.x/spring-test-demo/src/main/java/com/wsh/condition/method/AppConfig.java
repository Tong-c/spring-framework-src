package com.wsh.condition.method;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

	// 只有当DataSourceOpenCondition的matches()方法返回true的时候，才会注册Bean
	@Conditional({DataSourceOpenCondition.class})
	@Bean
	public DataSource dataSource() {
		return new DruidDataSource();
	}

}
