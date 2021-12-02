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
public class DailySenderEntity extends BaseEntity {

	/**
	 * 用户自定义接收人
	 */
	private Long dsId;

	/**
	 * 发送人ID
	 */
	private Long dsUserId;


	 private String dsUserName;


	 /**
	 * 默认接收人ID
	 */
	private Long dsRecevicer;


	/**
	 * 接收类型：0:发送，1：抄送
	 */
	private Integer dsType;

	/**
	 * 创建人
	 */
	private Long dsCreateBy;

	/**
	 * 创建时间
	 */
	private Date dsCreateDate;

	/**
	 * 创建时间
	 */
	private Long dsUpdateBy;

	/**
	 * 更新时间
	 */
	private Date dsUdpateDate;

	/**
	 * 是否删除：0：未删除，1：已删除
	 */
	private Integer dsDel;

	private String userName;

	private String DsTypefy;

	 /**
	  * 接收人邮箱
	  */
	 private String dsSendEmail;



	public DailySenderEntity(){}

	/**
	 * 设置用户自定义接收人
	 */
	public void setDsId(Long dsId) {
		this.dsId = dsId;
	}

	/**
	 * 获取用户自定义接收人
	 */
	public Long getDsId() {
		return this.dsId;
	}

	/**
	 * 设置发送人ID
	 */
	public void setDsUserId(Long dsUserId) {
		this.dsUserId = dsUserId;
	}

	/**
	 * 获取发送人ID
	 */
	public Long getDsUserId() {
		return this.dsUserId;
	}

	/**
	 * 设置默认接收人ID
	 */
	public void setDsRecevicer(Long dsRecevicer) {
		this.dsRecevicer = dsRecevicer;
	}

	/**
	 * 获取默认接收人ID
	 */
	public Long getDsRecevicer() {
		return this.dsRecevicer;
	}

	/**
	 * 设置接收类型：0:发送，1：抄送
	 */
	public void setDsType(Integer dsType) {
		this.dsType = dsType;
	}

	/**
	 * 获取接收类型：0:发送，1：抄送
	 */
	public Integer getDsType() {
		return this.dsType;
	}

	/**
	 * 设置创建人
	 */
	public void setDsCreateBy(Long dsCreateBy) {
		this.dsCreateBy = dsCreateBy;
	}

	/**
	 * 获取创建人
	 */
	public Long getDsCreateBy() {
		return this.dsCreateBy;
	}

	/**
	 * 设置创建时间
	 */
	public void setDsCreateDate(Date dsCreateDate) {
		this.dsCreateDate = dsCreateDate;
	}

	/**
	 * 获取创建时间
	 */
	public Date getDsCreateDate() {
		return this.dsCreateDate;
	}

	/**
	 * 设置创建时间
	 */
	public void setDsUpdateBy(Long dsUpdateBy) {
		this.dsUpdateBy = dsUpdateBy;
	}

	/**
	 * 获取创建时间
	 */
	public Long getDsUpdateBy() {
		return this.dsUpdateBy;
	}


	 public Integer getDsDel() {
		 return dsDel;
	 }

	 public void setDsDel(Integer dsDel) {
		 this.dsDel = dsDel;
	 }

	 public String getDsUserName() {
		 return dsUserName;
	 }

	 public void setDsUserName(String dsUserName) {
		 this.dsUserName = dsUserName;
	 }

	 public Date getDsUdpateDate() {
		 return dsUdpateDate;
	 }

	 public void setDsUdpateDate(Date dsUdpateDate) {
		 this.dsUdpateDate = dsUdpateDate;
	 }

	 public String getUserName() {
		 return userName;
	 }

	 public void setUserName(String userName) {
		 this.userName = userName;
	 }

	 public String getDsTypefy() {
		 return DsTypefy;
	 }

	 public void setDsTypefy(String dsTypefy) {
		 DsTypefy = dsTypefy;
	 }

	 public String getDsSendEmail() {
		 return dsSendEmail;
	 }

	 public void setDsSendEmail(String dsSendEmail) {
		 this.dsSendEmail = dsSendEmail;
	 }
 }
