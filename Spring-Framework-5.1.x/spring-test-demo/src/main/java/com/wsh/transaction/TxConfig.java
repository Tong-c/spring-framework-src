package com.wsh.transaction;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

// 开启Spring注解版事务功能
@EnableTransactionManagement
@Configuration
@ComponentScan("com.wsh.transaction")
public class TxConfig {

	/**
	 * 数据源配置
	 */
	@Bean
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUsername("root");
		dataSource.setPassword("0905");
		dataSource.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=Asia/Shanghai");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		return dataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	/**
	 * 事务管理器
	 */
	@Bean
	public PlatformTransactionManager platformTransactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}
