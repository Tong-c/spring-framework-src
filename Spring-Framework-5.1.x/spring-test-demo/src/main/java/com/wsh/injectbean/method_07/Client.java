package com.wsh.injectbean.method_07;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 第七种方式：@Import + ImportBeanDefinitionRegistrar方式
 *
 * 可以自定义bean的名称、作用域等很多参数, 像我们常见的Spring Cloud中的Feign，就使用了ImportBeanDefinitionRegistrar
 * class FeignClientsRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
 * 		public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
 *         this.registerDefaultConfiguration(metadata, registry);
 *         this.registerFeignClients(metadata, registry);
 *     }
 * }
 */
public class Client {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		System.out.println(applicationContext.getBean("product"));
		System.out.println(applicationContext.getBean("user"));
	}
}
