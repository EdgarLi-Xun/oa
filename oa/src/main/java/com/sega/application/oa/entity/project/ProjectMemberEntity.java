package com.sega.application.oa.entity.project;

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
public class ProjectMemberEntity extends BaseEntity {

	/**
	 *
	 */
	private Long pmId;

	/**
	 * 用户Id
	 */
	private Long pmUserId;

	/**
	 * 用户名称
	 */
	private String pmUserName;

	/**
	 * 项目Id
	 */
	private Long pmProjectId;

	/**
	 * 项目创建人
	 */
	private Long pmCreateBy;

	/**
	 *
	 */
	private Date pmCreateDate;

	/**
	 * 项目更新人
	 */
	private Long pmUpdateBy;

	/**
	 * 更新时间
	 */
	private Date pmUpdateDate;

	/**
	 * 是否删除，0:未删除，1：已删除
	 */
	private Integer pmDel;


	public ProjectMemberEntity(){}

	/**
	 * 设置
	 */
	public void setPmId(Long pmId) {
		this.pmId = pmId;
	}

	/**
	 * 获取
	 */
	public Long getPmId() {
		return this.pmId;
	}

	/**
	 * 设置用户Id
	 */
	public void setPmUserId(Long pmUserId) {
		this.pmUserId = pmUserId;
	}

	/**
	 * 获取用户Id
	 */
	public Long getPmUserId() {
		return this.pmUserId;
	}

	/**
	 * 设置用户名称
	 */
	public void setPmUserName(String pmUserName) {
		this.pmUserName = pmUserName;
	}

	/**
	 * 获取用户名称
	 */
	public String getPmUserName() {
		return this.pmUserName;
	}

	/**
	 * 设置项目Id
	 */
	public void setPmProjectId(Long pmProjectId) {
		this.pmProjectId = pmProjectId;
	}

	/**
	 * 获取项目Id
	 */
	public Long getPmProjectId() {
		return this.pmProjectId;
	}

	/**
	 * 设置项目创建人
	 */
	public void setPmCreateBy(Long pmCreateBy) {
		this.pmCreateBy = pmCreateBy;
	}

	/**
	 * 获取项目创建人
	 */
	public Long getPmCreateBy() {
		return this.pmCreateBy;
	}

	/**
	 * 设置
	 */
	public void setPmCreateDate(Date pmCreateDate) {
		this.pmCreateDate = pmCreateDate;
	}

	/**
	 * 获取
	 */
	public Date getPmCreateDate() {
		return this.pmCreateDate;
	}

	/**
	 * 设置项目更新人
	 */
	public void setPmUpdateBy(Long pmUpdateBy) {
		this.pmUpdateBy = pmUpdateBy;
	}

	/**
	 * 获取项目更新人
	 */
	public Long getPmUpdateBy() {
		return this.pmUpdateBy;
	}

	/**
	 * 设置更新时间
	 */
	public void setPmUpdateDate(Date pmUpdateDate) {
		this.pmUpdateDate = pmUpdateDate;
	}

	/**
	 * 获取更新时间
	 */
	public Date getPmUpdateDate() {
		return this.pmUpdateDate;
	}

	/**
	 * 设置是否删除，0:未删除，1：已删除
	 */
	public void setPmDel(Integer pmDel) {
		this.pmDel = pmDel;
	}

	/**
	 * 获取是否删除，0:未删除，1：已删除
	 */
	public Integer getPmDel() {
		return this.pmDel;
	}

}
