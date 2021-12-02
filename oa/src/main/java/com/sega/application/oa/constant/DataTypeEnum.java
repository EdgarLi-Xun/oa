package com.sega.application.oa.constant;

/**
 * 数据权限枚举类
 */
public enum DataTypeEnum {
    /**
     * 个人数据权限
     */
    personal(0),

    /**
     * 部门级数据权限
     */
    department(1),

    /**
     * 所在部门及以下
     */
    departmentAndSubordinate(2),

    /**
     * 全部
     */

    all(3);


    private Integer number;

    DataTypeEnum(Integer number){
        this.setNumber(number) ;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

}
