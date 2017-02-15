package com.huoyun.core.bo.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BoEntity {

	String namespace() default "com.huoyun.sbo";

	String name() default "";
	
	String label() default "";

	boolean exposed() default true;

	boolean creatable() default true;

	boolean readable() default true;

	boolean updatable() default true;

	boolean deletable() default true;
}
