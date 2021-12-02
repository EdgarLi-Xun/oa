package com.sega.application.oa.entity.administration;

import java.util.Date;

import com.sega.application.oa.entity.system.BaseEntity;


/**
 * 实体
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2019-12-12 11:44:56<br/>
 * 历史修订：<br/>
 */
public class UserVacationEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private Long uvId;
	
	/**
	 * 关联员工ID
	 */
	private Long uvUserId;
	
	/**
	 * 休假天数，可以是复数
	 */
	private Integer uvDays;
	
	/**
	 * 0:年假，1:调休，2：请假，3：加班
	 */
	private Integer uvType;
	
	/**
	 * 
	 */
	private Date uvStartDate;
	
	/**
	 * 
	 */
	private Date uvEndDate;
	
	/**
	 * 
	 */
	private Long uvCreateBy;
	
	/**
	 * 
	 */
	private Date uvCreateDate;
	
	/**
	 * 
	 */
	private Date uvUpdateDate;
	
	/**
	 * 
	 */
	private Long uvUpdateBy;
	
	/**
	 * 是否删除：未删除,1：已删除
	 */
	private Integer uvDel;
	
	public UserVacationEntity(){}
	
	/**
	 * 设置
	 */
	public void setUvId(Long uvId) {
		this.uvId = uvId;
	}

	/**
	 * 获取
	 */
	public Long getUvId() {
		return this.uvId;
	}
	
	/**
	 * 设置关联员工ID
	 */
	public void setUvUserId(Long uvUserId) {
		this.uvUserId = uvUserId;
	}

	/**
	 * 获取关联员工ID
	 */
	public Long getUvUserId() {
		return this.uvUserId;
	}
	
	/**
	 * 设置休假天数，可以是复数
	 */
	public void setUvDays(Integer uvDays) {
		this.uvDays = uvDays;
	}

	/**
	 * 获取休假天数，可以是复数
	 */
	public Integer getUvDays() {
		return this.uvDays;
	}
	
	/**
	 * 设置0:年假，1:调休，2：请假，3：加班
	 */
	public void setUvType(Integer uvType) {
		this.uvType = uvType;
	}

	/**
	 * 获取0:年假，1:调休，2：请假，3：加班
	 */
	public Integer getUvType() {
		return this.uvType;
	}
	
	/**
	 * 设置
	 */
	public void setUvStartDate(Date uvStartDate) {
		this.uvStartDate = uvStartDate;
	}

	/**
	 * 获取
	 */
	public Date getUvStartDate() {
		return this.uvStartDate;
	}
	
	/**
	 * 设置
	 */
	public void setUvEndDate(Date uvEndDate) {
		this.uvEndDate = uvEndDate;
	}

	/**
	 * 获取
	 */
	public Date getUvEndDate() {
		return this.uvEndDate;
	}
	
	/**
	 * 设置
	 */
	public void setUvCreateBy(Long uvCreateBy) {
		this.uvCreateBy = uvCreateBy;
	}

	/**
	 * 获取
	 */
	public Long getUvCreateBy() {
		return this.uvCreateBy;
	}
	
	/**
	 * 设置
	 */
	public void setUvCreateDate(Date uvCreateDate) {
		this.uvCreateDate = uvCreateDate;
	}

	/**
	 * 获取
	 */
	public Date getUvCreateDate() {
		return this.uvCreateDate;
	}
	
	/**
	 * 设置
	 */
	public void setUvUpdateDate(Date uvUpdateDate) {
		this.uvUpdateDate = uvUpdateDate;
	}

	/**
	 * 获取
	 */
	public Date getUvUpdateDate() {
		return this.uvUpdateDate;
	}
	
	/**
	 * 设置
	 */
	public void setUvUpdateBy(Long uvUpdateBy) {
		this.uvUpdateBy = uvUpdateBy;
	}

	/**
	 * 获取
	 */
	public Long getUvUpdateBy() {
		return this.uvUpdateBy;
	}
	
	/**
	 * 设置是否删除：未删除,1：已删除
	 */
	public void setUvDel(Integer uvDel) {
		this.uvDel = uvDel;
	}

	/**
	 * 获取是否删除：未删除,1：已删除
	 */
	public Integer getUvDel() {
		return this.uvDel;
	}
	
}