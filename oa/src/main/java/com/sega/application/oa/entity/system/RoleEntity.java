package com.sega.application.oa.entity.system;

import java.util.Date;

/**
* 实体
* @author 邱小兵
* @version
* 版本号：1.0.0<br/>
* 创建日期：2017-8-3 10:27:05<br/>
* 历史修订：<br/>
*/
public class RoleEntity extends BaseEntity {

   /**
    * 角色id
    */
   private Long roleId;
   /**
    * 角色名称
    */
   private String roleName;
   /**
    * 角色类别：0用户，1管理员
    */
   private Integer roleType;
   /**
    * 角色数据权限类别
    */
   private Integer roleDataType;
   /**
    * 角色描述
    */
   private String roleDescribe;
   /**
    * 创建人
    */
   private Long createBy;
   /**
    * 创建时间
    */
   private Date createDate;
   /**
    * 更新人
    */
   private Long updateBy;
   /**
    * 更新时间
    */
   private Date updateDate;
   /**
    * 是否已删除: 0：未删除，1：已删除
    */
   private Integer roleDel;

   public RoleEntity(){}

   /**
    * 设置角色id
    */
   public void setRoleId(Long roleId) {
       this.roleId = roleId;
   }

   /**
    * 获取角色id
    */
   public Long getRoleId() {
       return this.roleId;
   }

   /**
    * 设置角色名称
    */
   public void setRoleName(String roleName) {
       this.roleName = roleName;
   }

   /**
    * 获取角色名称
    */
   public String getRoleName() {
       return this.roleName;
   }

   /**
    * 设置角色类别：0用户，1管理员
    */
   public void setRoleType(Integer roleType) {
       this.roleType = roleType;
   }

   /**
    * 获取角色类别：0用户，1管理员
    */
   public Integer getRoleType() {
       return this.roleType;
   }

   /**
    * 设置角色描述
    */
   public void setRoleDescribe(String roleDescribe) {
       this.roleDescribe = roleDescribe;
   }

   /**
    * 获取角色描述
    */
   public String getRoleDescribe() {
       return this.roleDescribe;
   }

   /**
    * 设置创建人
    */
   public void setCreateBy(Long createBy) {
       this.createBy = createBy;
   }

   /**
    * 获取创建人
    */
   public Long getCreateBy() {
       return this.createBy;
   }

   /**
    * 设置创建时间
    */
   public void setCreateDate(Date createDate) {
       this.createDate = createDate;
   }

   /**
    * 获取创建时间
    */
   public Date getCreateDate() {
       return this.createDate;
   }

   /**
    * 设置更新人
    */
   public void setUpdateBy(Long updateBy) {
       this.updateBy = updateBy;
   }

   /**
    * 获取更新人
    */
   public Long getUpdateBy() {
       return this.updateBy;
   }

   /**
    * 设置更新时间
    */
   public void setUpdateDate(Date updateDate) {
       this.updateDate = updateDate;
   }

   /**
    * 获取更新时间
    */
   public Date getUpdateDate() {
       return this.updateDate;
   }

   /**
    * 设置是否已删除: 0：未删除，1：已删除
    */
   public void setRoleDel(Integer roleDel) {
       this.roleDel = roleDel;
   }

   /**
    * 获取是否已删除: 0：未删除，1：已删除
    */
   public Integer getRoleDel() {
       return this.roleDel;
   }

   public Integer getRoleDataType() {
       return roleDataType;
   }

   public void setRoleDataType(Integer roleDataType) {
       this.roleDataType = roleDataType;
   }

}