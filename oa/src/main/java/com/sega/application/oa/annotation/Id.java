package com.sega.application.oa.annotation;

import java.lang.annotation.*;

/**
 * 实体映射主键注解
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.FIELD})  
@Documented
public @interface Id {

	/**
	 * 主键名
	 * @return
	 */
	String value() default "";
}
