package com.sega.application.oa.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * activity 注解信息
 * @author 孙乾
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface WorkFlow {
    /**
     * 表名
     * @return
     */
    String table() ;

    /**
     * 表Id
     * @return
     */
    String tableId();

    /**
     * 流程状态字段
     * @return
     */
    String statusColumn() ;

    /**
     * 流程标识
     * @return
     */
    String procDefKey() ;

    /**
     * 流程列名
     * @return
     */
    String procInstColumn() ;
}
