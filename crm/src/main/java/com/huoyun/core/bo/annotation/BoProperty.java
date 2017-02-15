package com.huoyun.core.bo.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface BoProperty {

	String label() default "";
	
	boolean exposed() default true;
	
	boolean mandatory() default false;
	
	boolean readonly() default false;
}
