package com.sega.application.oa.entity.system;

/**
 * 微信审批消息的实体类
 *
 * @author sunqian
 * @date 2019-12-14
 */
public class WechatEntity {

    /**
     * 开始时间
     * Unix时间戳
     */
    private String startTime;

    /**
     * 结束时间
     * Unix时间戳
     */
    private String endTime;

    /**
     * 审批模板ID
     */
    private String temleteId;

    /**
     * 申请人名称
     */
    private String applyName;

    /**
     * 流程名称
     */
    private String spName;

    /*
     * 审批单号
     */
    private String spNum;

    /**
     * 所属部门
     */
    private String org;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTemleteId() {
        return temleteId;
    }

    public void setTemleteId(String temleteId) {
        this.temleteId = temleteId;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getSpNum() {
        return spNum;
    }

    public void setSpNum(String spNum) {
        this.spNum = spNum;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }
}
