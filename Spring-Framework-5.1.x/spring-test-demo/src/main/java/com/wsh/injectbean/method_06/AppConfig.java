package com.wsh.injectbean.method_06;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({MyImportSelector.class})
@Configuration
public class AppConfig {

}
