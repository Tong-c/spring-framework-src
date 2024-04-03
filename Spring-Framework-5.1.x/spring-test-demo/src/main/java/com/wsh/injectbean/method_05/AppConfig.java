package com.wsh.injectbean.method_05;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({Student.class})
@Configuration
public class AppConfig {

}
