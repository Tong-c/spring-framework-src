package com.wsh.injectbean.method_06;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 第六种方式：@Import + ImportSelector方式
 * <p>
 * ImportSelector接口的好处主要有以下两点：
 * <p>
 * 1.把某个功能的相关类放到一起，方面管理和维护。
 * 2.重写selectImports方法时，能够根据条件判断某些类是否需要被实例化，或者某个条件实例化这些bean，其他的条件实例化那些bean等,我们能够非常灵活的定制化bean的实例化。
 */
public class Client {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		for (String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
			System.out.println(beanDefinitionName);
		}
		System.out.println("================");
		System.out.println(applicationContext.getBean("com.wsh.injectbean.method_06.User"));
		try {
			System.out.println(applicationContext.getBean("user"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
