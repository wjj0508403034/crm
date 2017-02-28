package com.huoyun.core.bo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.huoyun.core.bo.validator.RuleType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface BoPropertyRule {

	RuleType rule();

	String expr();
}
