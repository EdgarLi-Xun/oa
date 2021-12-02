package com.sega.application.oa.annotation;

import java.lang.annotation.*;

/**
 * 日志模块注解
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.TYPE})  
@Documented
public @interface Module {

	/**
	 * 模块名
	 * @return
	 */
	String value() default "";
	
}
