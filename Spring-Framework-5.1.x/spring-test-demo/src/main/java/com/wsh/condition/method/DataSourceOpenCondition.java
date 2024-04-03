package com.wsh.condition.method;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class DataSourceOpenCondition implements Condition {

	private static final String IS_OPEN_PROPERTY = "is.import.durid.datasource";

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String isOpenFlag = context.getEnvironment().getProperty(IS_OPEN_PROPERTY);
		return "true".equalsIgnoreCase(isOpenFlag);
	}

}
