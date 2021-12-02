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
public class DailyReceivcerEntity extends BaseEntity {
	
	/**
	 * 日报接收ID
	 */
	private Long drId;
	
	/**
	 * 日报ID
	 */
	private Long drDailyId;
	
	/**
	 * 日报接收人ID
	 */
	private Long drUserId;
	
	/**
	 * 创建人
	 */
	private Long drCreateBy;
	
	/**
	 * 创建时间
	 */
	private Date drCreateDate;
	
	/**
	 * 更新人
	 */
	private Long drUpdateBy;
	
	/**
	 * 更新时间
	 */
	private Date drUpdateDate;
	
	/**
	 * 是否删除：0：未删除，1：已删除
	 */
	private Integer drDel;
	
	public DailyReceivcerEntity(){}
	
	/**
	 * 设置日报接收ID
	 */
	public void setDrId(Long drId) {
		this.drId = drId;
	}

	/**
	 * 获取日报接收ID
	 */
	public Long getDrId() {
		return this.drId;
	}
	
	/**
	 * 设置日报ID
	 */
	public void setDrDailyId(Long drDailyId) {
		this.drDailyId = drDailyId;
	}

	/**
	 * 获取日报ID
	 */
	public Long getDrDailyId() {
		return this.drDailyId;
	}
	
	/**
	 * 设置日报接收人ID
	 */
	public void setDrUserId(Long drUserId) {
		this.drUserId = drUserId;
	}

	/**
	 * 获取日报接收人ID
	 */
	public Long getDrUserId() {
		return this.drUserId;
	}
	
	/**
	 * 设置创建人
	 */
	public void setDrCreateBy(Long drCreateBy) {
		this.drCreateBy = drCreateBy;
	}

	/**
	 * 获取创建人
	 */
	public Long getDrCreateBy() {
		return this.drCreateBy;
	}
	
	/**
	 * 设置创建时间
	 */
	public void setDrCreateDate(Date drCreateDate) {
		this.drCreateDate = drCreateDate;
	}

	/**
	 * 获取创建时间
	 */
	public Date getDrCreateDate() {
		return this.drCreateDate;
	}
	
	/**
	 * 设置更新人
	 */
	public void setDrUpdateBy(Long drUpdateBy) {
		this.drUpdateBy = drUpdateBy;
	}

	/**
	 * 获取更新人
	 */
	public Long getDrUpdateBy() {
		return this.drUpdateBy;
	}
	
	/**
	 * 设置更新时间
	 */
	public void setDrUpdateDate(Date drUpdateDate) {
		this.drUpdateDate = drUpdateDate;
	}

	/**
	 * 获取更新时间
	 */
	public Date getDrUpdateDate() {
		return this.drUpdateDate;
	}
	
	/**
	 * 设置是否删除：0：未删除，1：已删除
	 */
	public void setDrDel(Integer drDel) {
		this.drDel = drDel;
	}

	/**
	 * 获取是否删除：0：未删除，1：已删除
	 */
	public Integer getDrDel() {
		return this.drDel;
	}
	
}