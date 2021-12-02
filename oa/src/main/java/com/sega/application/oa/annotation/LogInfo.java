package com.sega.application.oa.annotation;

import java.lang.annotation.*;

/**
 * 日志操作注解
 * @author 孙乾
 * {@value operation 操作方法, table 操作表}
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.METHOD})  
@Documented
public @interface LogInfo {

	String description() default "";
	
	String table() default "";
	
}
