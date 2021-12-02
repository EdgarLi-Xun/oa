package com.sega.application.oa.entity.daily;

import java.util.Date;
import com.sega.application.oa.entity.system.BaseEntity;


 /**
 * 实体
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2020-3-4 17:09:42<br/>
 * 历史修订：<br/>
 */
public class DailyfileEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private Long dailyfileId;
	
	/**
	 * 
	 */
	private String dailyfileName;
	
	/**
	 * 
	 */
	private String dailyfileRealName;
	
	/**
	 * 
	 */
	private String dailyfileType;
	
	/**
	 * 
	 */
	private String dailyfilePath;
	
	/**
	 * 
	 */
	private Long dailyfileCreateBy;
	
	/**
	 * 
	 */
	private Date dailyfileCreateDate;
	
	/**
	 * 
	 */
	private Long dailyfileUpdateBy;
	
	/**
	 * 
	 */
	private Date dailyfileUpdateDate;
	
	/**
	 * 
	 */
	private Integer dailyfileDel;

	private Long dailyfileDailyid;
	
	public DailyfileEntity(){}

	
	/**
	 * 设置
	 */
	public void setDailyfileId(Long dailyfileId) {
		this.dailyfileId = dailyfileId;
	}

	/**
	 * 获取
	 */
	public Long getDailyfileId() {
		return this.dailyfileId;
	}
	
	/**
	 * 设置
	 */
	public void setDailyfileName(String dailyfileName) {
		this.dailyfileName = dailyfileName;
	}

	/**
	 * 获取
	 */
	public String getDailyfileName() {
		return this.dailyfileName;
	}
	
	/**
	 * 设置
	 */
	public void setDailyfileRealName(String dailyfileRealName) {
		this.dailyfileRealName = dailyfileRealName;
	}

	/**
	 * 获取
	 */
	public String getDailyfileRealName() {
		return this.dailyfileRealName;
	}
	
	/**
	 * 设置
	 */
	public void setDailyfileType(String dailyfileType) {
		this.dailyfileType = dailyfileType;
	}

	/**
	 * 获取
	 */
	public String getDailyfileType() {
		return this.dailyfileType;
	}
	
	/**
	 * 设置
	 */
	public void setDailyfilePath(String dailyfilePath) {
		this.dailyfilePath = dailyfilePath;
	}

	/**
	 * 获取
	 */
	public String getDailyfilePath() {
		return this.dailyfilePath;
	}
	
	/**
	 * 设置
	 */
	public void setDailyfileCreateBy(Long dailyfileCreateBy) {
		this.dailyfileCreateBy = dailyfileCreateBy;
	}

	/**
	 * 获取
	 */
	public Long getDailyfileCreateBy() {
		return this.dailyfileCreateBy;
	}
	
	/**
	 * 设置
	 */
	public void setDailyfileCreateDate(Date dailyfileCreateDate) {
		this.dailyfileCreateDate = dailyfileCreateDate;
	}

	/**
	 * 获取
	 */
	public Date getDailyfileCreateDate() {
		return this.dailyfileCreateDate;
	}
	
	/**
	 * 设置
	 */
	public void setDailyfileUpdateBy(Long dailyfileUpdateBy) {
		this.dailyfileUpdateBy = dailyfileUpdateBy;
	}

	/**
	 * 获取
	 */
	public Long getDailyfileUpdateBy() {
		return this.dailyfileUpdateBy;
	}
	
	/**
	 * 设置
	 */
	public void setDailyfileUpdateDate(Date dailyfileUpdateDate) {
		this.dailyfileUpdateDate = dailyfileUpdateDate;
	}

	/**
	 * 获取
	 */
	public Date getDailyfileUpdateDate() {
		return this.dailyfileUpdateDate;
	}
	
	/**
	 * 设置
	 */
	public void setDailyfileDel(Integer dailyfileDel) {
		this.dailyfileDel = dailyfileDel;
	}

	/**
	 * 获取
	 */
	public Integer getDailyfileDel() {
		return this.dailyfileDel;
	}


	 public Long getDailyfileDailyid() {
		 return dailyfileDailyid;
	 }

	 public void setDailyfileDailyid(Long dailyfileDailyid) {
		 this.dailyfileDailyid = dailyfileDailyid;
	 }
 }