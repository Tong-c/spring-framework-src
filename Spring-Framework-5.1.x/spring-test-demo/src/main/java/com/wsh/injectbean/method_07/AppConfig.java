package com.wsh.injectbean.method_07;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({CustomImportBeanDefinitionRegistrar.class})
@Configuration
public class AppConfig {

}
