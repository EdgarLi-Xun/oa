package com.sega.application.oa.entity.system;

import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * 输出的信息
 * 用于返回数据到前端
 * @author  sq
 */
public class OutData {

    /**
     * 是否成功标识
     */
    private Boolean success = true;


    /**
     * 返回的页面提示信息
     */
    private String message = "成功" ;

    /**
     * 返回的数据对象
     */
    private Object obj;

    public OutData(){

    }

    /**
     * 只返回一个标识
     * @param success
     */
    public OutData(Boolean success){
        this.success = success;
    }

    /**
     * 返回结果集合，其他用默认信息
     * @param obj
     */
    public OutData(Object obj){
        this.obj = obj;
    }

    /**
     * 返回提示信息，其他用默认信息
     * @param msg
     */
    public OutData(String msg){
        this.message = msg;
    }

    /**
     * 返回标识和提示信息
     * @param success
     * @param message
     */
    public OutData(Boolean success,String message){
        this.success = success;
        this.message = message;
    }

    /**
     * 返回标识，提示信息，以及对应的结果集
     * @param success
     * @param message
     * @param obj
     */
    public OutData(Boolean success,String message,Object obj){
        this.success = success;
        this.message = message;
        this.obj = obj;
    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
