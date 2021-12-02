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
public class DailyPlanEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private Long dpId;
	
	/**
	 * 项目id
	 */
	private Long dpProjectId;
	
	/**
	 * 日报id
	 */
	private Long dpDailyId;
	
	/**
	 * 明日计划
	 */
	private String dpPlanContent;
	
	/**
	 * 计划耗时
	 */
	private String dpPlanTime;
	
	/**
	 * 备注
	 */
	private String dpPlanRemarks;
	
	/**
	 * 创建人
	 */
	private Long dpCreateBy;
	
	/**
	 * 创建时间
	 */
	private Date dpCreateDate;
	
	/**
	 * 更新人
	 */
	private Long dpUpdateBy;
	
	/**
	 * 更新时间
	 */
	private Date dpUpdateDate;
	
	/**
	 * 是否已删除：0：未删除，1已删除
	 */
	private Integer dpDel;

	private String dpProjectName;
	
	public DailyPlanEntity(){}
	
	/**
	 * 设置
	 */
	public void setDpId(Long dpId) {
		this.dpId = dpId;
	}

	/**
	 * 获取
	 */
	public Long getDpId() {
		return this.dpId;
	}
	
	/**
	 * 设置项目id
	 */
	public void setDpProjectId(Long dpProjectId) {
		this.dpProjectId = dpProjectId;
	}

	/**
	 * 获取项目id
	 */
	public Long getDpProjectId() {
		return this.dpProjectId;
	}
	
	/**
	 * 设置日报id
	 */
	public void setDpDailyId(Long dpDailyId) {
		this.dpDailyId = dpDailyId;
	}

	/**
	 * 获取日报id
	 */
	public Long getDpDailyId() {
		return this.dpDailyId;
	}
	
	/**
	 * 设置明日计划
	 */
	public void setDpPlanContent(String dpPlanContent) {
		this.dpPlanContent = dpPlanContent;
	}

	/**
	 * 获取明日计划
	 */
	public String getDpPlanContent() {
		return this.dpPlanContent;
	}

	 public String getDpPlanTime() {
		 return dpPlanTime;
	 }

	 public void setDpPlanTime(String dpPlanTime) {
		 this.dpPlanTime = dpPlanTime;
	 }

	 /**
	 * 设置备注
	 */
	public void setDpPlanRemarks(String dpPlanRemarks) {
		this.dpPlanRemarks = dpPlanRemarks;
	}

	/**
	 * 获取备注
	 */
	public String getDpPlanRemarks() {
		return this.dpPlanRemarks;
	}
	
	/**
	 * 设置创建人
	 */
	public void setDpCreateBy(Long dpCreateBy) {
		this.dpCreateBy = dpCreateBy;
	}

	/**
	 * 获取创建人
	 */
	public Long getDpCreateBy() {
		return this.dpCreateBy;
	}
	
	/**
	 * 设置创建时间
	 */
	public void setDpCreateDate(Date dpCreateDate) {
		this.dpCreateDate = dpCreateDate;
	}

	/**
	 * 获取创建时间
	 */
	public Date getDpCreateDate() {
		return this.dpCreateDate;
	}
	
	/**
	 * 设置更新人
	 */
	public void setDpUpdateBy(Long dpUpdateBy) {
		this.dpUpdateBy = dpUpdateBy;
	}

	/**
	 * 获取更新人
	 */
	public Long getDpUpdateBy() {
		return this.dpUpdateBy;
	}
	
	/**
	 * 设置更新时间
	 */
	public void setDpUpdateDate(Date dpUpdateDate) {
		this.dpUpdateDate = dpUpdateDate;
	}

	/**
	 * 获取更新时间
	 */
	public Date getDpUpdateDate() {
		return this.dpUpdateDate;
	}
	
	/**
	 * 设置是否已删除：0：未删除，1已删除
	 */
	public void setDpDel(Integer dpDel) {
		this.dpDel = dpDel;
	}

	/**
	 * 获取是否已删除：0：未删除，1已删除
	 */
	public Integer getDpDel() {
		return this.dpDel;
	}

	 public String getDpProjectName() {
		 return dpProjectName;
	 }

	 public void setDpProjectName(String dpProjectName) {
		 this.dpProjectName = dpProjectName;
	 }
 }