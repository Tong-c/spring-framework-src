package com.wsh.injectbean.method_02;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 第二种方式： 使用@Component注解 + @ComponentScan包扫描方式
 * 包括@Controler、@Service、@Repository等派生的注解。
 * 为了解决bean太多时，xml文件过大，从而导致膨胀不好维护的问题。在spring2.5中开始支持：@Component、@Repository、@Service、@Controller等注解定义bean。
 * 其实本质上@Controler、@Service、@Repository也是使用@Component注解修饰的。
 * <p>
 * 通常情况下：
 *
 * @Controller：一般用在控制层
 * @Service：一般用在业务层
 * @Repository：一般用在持久层
 * @Component：一般用在公共组件上
 */
public class Client {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		System.out.println(applicationContext.getBean("userDao"));
		System.out.println(applicationContext.getBean("userService"));
		System.out.println(applicationContext.getBean("userController"));
		System.out.println(applicationContext.getBean("userHandler"));
	}
}
