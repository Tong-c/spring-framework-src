package com.wsh.injectbean.method_07;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class CustomImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		// 可以自定义bean的名称、作用域等很多参数
		registry.registerBeanDefinition("user", new RootBeanDefinition(User.class));
		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(Product.class);
		rootBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
		registry.registerBeanDefinition("product", rootBeanDefinition);
	}
}
