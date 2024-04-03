package com.wsh.injectbean.method_06;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportSelector implements ImportSelector {

	// 指定需要定义bean的类名，注意要包含完整路径，而非相对路径
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[]{"com.wsh.injectbean.method_06.Product", "com.wsh.injectbean.method_06.User"};
	}

}
