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
public class FixedAssetsEntity extends BaseEntity {
	
	/**
	 * 固定资产Id
	 */
	private Long faId;
	
	/**
	 * 固定资产名称
	 */
	private String faName;
	
	/**
	 * 固定资产编号
	 */
	private String faCode;
	
	/**
	 * 责任人Id
	 */
	private Long faResponsibleId;
	
	/**
	 * 责任人名称
	 */
	private String faResponsibleName;
	
	/**
	 * 价格
	 */
	private Double faPrice;
	
	/**
	 * 备注
	 */
	private String faRemark;
	
	/**
	 * 资产状态：0：在用，1：丢失，2：停用，3：维修，4：外借
	 */
	private Long faState;
	
	/**
	 * 创建人
	 */
	private Long faCreateBy;
	
	/**
	 * 创建时间
	 */
	private Date faCreateDate;
	
	/**
	 * 更新人
	 */
	private Long faUpdateBy;
	
	/**
	 * 更新时间
	 */
	private Date faUpdateDate;
	
	/**
	 * 是否删除：0：未删除，1：已删除
	 */
	private Integer faDel;
	
	public FixedAssetsEntity(){}
	
	/**
	 * 设置固定资产Id
	 */
	public void setFaId(Long faId) {
		this.faId = faId;
	}

	/**
	 * 获取固定资产Id
	 */
	public Long getFaId() {
		return this.faId;
	}
	
	/**
	 * 设置固定资产名称
	 */
	public void setFaName(String faName) {
		this.faName = faName;
	}

	/**
	 * 获取固定资产名称
	 */
	public String getFaName() {
		return this.faName;
	}
	
	/**
	 * 设置固定资产编号
	 */
	public void setFaCode(String faCode) {
		this.faCode = faCode;
	}

	/**
	 * 获取固定资产编号
	 */
	public String getFaCode() {
		return this.faCode;
	}
	
	/**
	 * 设置责任人Id
	 */
	public void setFaResponsibleId(Long faResponsibleId) {
		this.faResponsibleId = faResponsibleId;
	}

	/**
	 * 获取责任人Id
	 */
	public Long getFaResponsibleId() {
		return this.faResponsibleId;
	}
	
	/**
	 * 设置责任人名称
	 */
	public void setFaResponsibleName(String faResponsibleName) {
		this.faResponsibleName = faResponsibleName;
	}

	/**
	 * 获取责任人名称
	 */
	public String getFaResponsibleName() {
		return this.faResponsibleName;
	}
	
	/**
	 * 设置价格
	 */
	public void setFaPrice(Double faPrice) {
		this.faPrice = faPrice;
	}

	/**
	 * 获取价格
	 */
	public Double getFaPrice() {
		return this.faPrice;
	}
	
	/**
	 * 设置备注
	 */
	public void setFaRemark(String faRemark) {
		this.faRemark = faRemark;
	}

	/**
	 * 获取备注
	 */
	public String getFaRemark() {
		return this.faRemark;
	}
	
	/**
	 * 设置资产状态：0：在用，1：丢失，2：停用，3：维修，4：外借
	 */
	public void setFaState(Long faState) {
		this.faState = faState;
	}

	/**
	 * 获取资产状态：0：在用，1：丢失，2：停用，3：维修，4：外借
	 */
	public Long getFaState() {
		return this.faState;
	}
	
	/**
	 * 设置创建人
	 */
	public void setFaCreateBy(Long faCreateBy) {
		this.faCreateBy = faCreateBy;
	}

	/**
	 * 获取创建人
	 */
	public Long getFaCreateBy() {
		return this.faCreateBy;
	}
	
	/**
	 * 设置创建时间
	 */
	public void setFaCreateDate(Date faCreateDate) {
		this.faCreateDate = faCreateDate;
	}

	/**
	 * 获取创建时间
	 */
	public Date getFaCreateDate() {
		return this.faCreateDate;
	}
	
	/**
	 * 设置更新人
	 */
	public void setFaUpdateBy(Long faUpdateBy) {
		this.faUpdateBy = faUpdateBy;
	}

	/**
	 * 获取更新人
	 */
	public Long getFaUpdateBy() {
		return this.faUpdateBy;
	}
	
	/**
	 * 设置更新时间
	 */
	public void setFaUpdateDate(Date faUpdateDate) {
		this.faUpdateDate = faUpdateDate;
	}

	/**
	 * 获取更新时间
	 */
	public Date getFaUpdateDate() {
		return this.faUpdateDate;
	}
	
	/**
	 * 设置是否删除：0：未删除，1：已删除
	 */
	public void setFaDel(Integer faDel) {
		this.faDel = faDel;
	}

	/**
	 * 获取是否删除：0：未删除，1：已删除
	 */
	public Integer getFaDel() {
		return this.faDel;
	}
	
}