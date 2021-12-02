package com.sega.application.oa.entity.daily;

import java.util.Date;

import com.sega.application.oa.entity.system.BaseEntity;


 /**
 * 实体
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:22:36<br/>
 * 历史修订：<br/>
 */
public class DailyHazardEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private Long dhId;
	
	/**
	 * 日报Id
	 */
	private Long dhDailyId;
	
	/**
	 * 问题描述
	 */
	private String dhProblem;
	
	/**
	 * 应对方案
	 */
	private String dhPlan;
	
	/**
	 * 创建人
	 */
	private Long dhCreateBy;
	
	/**
	 * 创建时间
	 */
	private Date dhCreateDate;
	
	/**
	 * 更新人
	 */
	private Long dhUpdateBy;
	
	/**
	 * 更新时间
	 */
	private Date dhUpdateDate;
	
	/**
	 * 是否删除，0:未删除，1：已删除
	 */
	private Integer dhDel;
	
	public DailyHazardEntity(){}
	
	/**
	 * 设置
	 */
	public void setDhId(Long dhId) {
		this.dhId = dhId;
	}

	/**
	 * 获取
	 */
	public Long getDhId() {
		return this.dhId;
	}
	
	/**
	 * 设置日报Id
	 */
	public void setDhDailyId(Long dhDailyId) {
		this.dhDailyId = dhDailyId;
	}

	/**
	 * 获取日报Id
	 */
	public Long getDhDailyId() {
		return this.dhDailyId;
	}
	
	/**
	 * 设置问题描述
	 */
	public void setDhProblem(String dhProblem) {
		this.dhProblem = dhProblem;
	}

	/**
	 * 获取问题描述
	 */
	public String getDhProblem() {
		return this.dhProblem;
	}
	
	/**
	 * 设置应对方案
	 */
	public void setDhPlan(String dhPlan) {
		this.dhPlan = dhPlan;
	}

	/**
	 * 获取应对方案
	 */
	public String getDhPlan() {
		return this.dhPlan;
	}
	
	/**
	 * 设置创建人
	 */
	public void setDhCreateBy(Long dhCreateBy) {
		this.dhCreateBy = dhCreateBy;
	}

	/**
	 * 获取创建人
	 */
	public Long getDhCreateBy() {
		return this.dhCreateBy;
	}
	
	/**
	 * 设置创建时间
	 */
	public void setDhCreateDate(Date dhCreateDate) {
		this.dhCreateDate = dhCreateDate;
	}

	/**
	 * 获取创建时间
	 */
	public Date getDhCreateDate() {
		return this.dhCreateDate;
	}
	
	/**
	 * 设置更新人
	 */
	public void setDhUpdateBy(Long dhUpdateBy) {
		this.dhUpdateBy = dhUpdateBy;
	}

	/**
	 * 获取更新人
	 */
	public Long getDhUpdateBy() {
		return this.dhUpdateBy;
	}
	
	/**
	 * 设置更新时间
	 */
	public void setDhUpdateDate(Date dhUpdateDate) {
		this.dhUpdateDate = dhUpdateDate;
	}

	/**
	 * 获取更新时间
	 */
	public Date getDhUpdateDate() {
		return this.dhUpdateDate;
	}
	
	/**
	 * 设置是否删除，0:未删除，1：已删除
	 */
	public void setDhDel(Integer dhDel) {
		this.dhDel = dhDel;
	}

	/**
	 * 获取是否删除，0:未删除，1：已删除
	 */
	public Integer getDhDel() {
		return this.dhDel;
	}
	
}