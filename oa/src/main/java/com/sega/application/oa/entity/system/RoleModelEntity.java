package com.sega.application.oa.entity.system;

/**
 * 实体
 * @author 邱小兵
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2017-8-1 21:09:59<br/>
 * 历史修订：<br/>
 */
public class RoleModelEntity extends BaseEntity {

	/**
	 * 角色编号
	 */
	private Long rmRoleId;
	/**
	 * 模块编号
	 */
	private Long rmModelId;

	/**
	 * 关联角色实体
	 */
	private RoleEntity role;

	/**
	 * 关联模块实体
	 */
	private ModelEntity model;

	public RoleModelEntity(){}

	public RoleModelEntity(Long rmRoleId) {
		super();
		this.rmRoleId = rmRoleId;
	}

	public RoleModelEntity(Long rmRoleId, Long rmModelId) {
		super();
		this.rmRoleId = rmRoleId;
		this.rmModelId = rmModelId;
	}

	/**
	 * 设置角色编号
	 */
	public void setRmRoleId(Long rmRoleId) {
		this.rmRoleId = rmRoleId;
	}

	/**
	 * 获取角色编号
	 */
	public Long getRmRoleId() {
		return this.rmRoleId;
	}

	/**
	 * 设置模块编号
	 */
	public void setRmModelId(Long rmModelId) {
		this.rmModelId = rmModelId;
	}

	/**
	 * 获取模块编号
	 */
	public Long getRmModelId() {
		return this.rmModelId;
	}

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	public ModelEntity getModel() {
		return model;
	}

	public void setModel(ModelEntity model) {
		this.model = model;
	}


}