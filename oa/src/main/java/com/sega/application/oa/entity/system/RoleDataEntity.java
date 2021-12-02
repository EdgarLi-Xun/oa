package com.sega.application.oa.entity.system;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
* 数据分配权限实体
* @author sunqian
* @version
* 版本号：1.0.0<br/>
* 创建日期：2017-8-9 17:34:14<br/>
* 历史修订：<br/>
*/
public class RoleDataEntity extends BaseEntity {

   /**
    * 数据权限主键
    */
   protected Integer rdId;

   /**
    * 数据ID
    */
   protected Integer rdValue;

   /**
    * 待办内容
    */
   protected String rdContent;

   /**
    * 需要查看的编号或名称
    */
   private String rdCodeOrNames;

   /**
    * 用户ID
    */
   protected Integer rdUserId;

   /**
    * 表名
    */
   protected String rdTable;

   /**
    * 创建人
    */
   protected Long createBy;

   /**
    * 更新时间
    */
   @DateTimeFormat(pattern="yyyy-MM-dd")
   protected Date createDate;

   /**
    * 更新人
    */
   protected Long updateBy;

   /**
    * 更新时间
    */
   @DateTimeFormat(pattern="yyyy-MM-dd")
   protected Date updateDate;

   /**
    * 表对应列名
    */
   protected String rdColumn;

   /**
    * 对应模块编码
    */
   protected String rdModel;

   /**
    * 未删除：0:未删除，1：已删除
    */
   protected Integer rdDel;

   /**
    * 分配用户id集合
    */
   protected String userIds;

   /**
    * 流程信息字段：0 新建 1办理
    */
   protected Integer process;

   /**
    * 查询条件
    */
   protected String querySql;

   public RoleDataEntity(){}

   public RoleDataEntity(Integer rdUserId, String rdTable) {
       super();
       this.rdUserId = rdUserId;
       this.rdTable = rdTable;
   }

   /**
    * 设置数据权限主键
    */
   public void setRdId(Integer rdId) {
       this.rdId = rdId;
   }

   /**
    * 获取数据权限主键
    */
   public Integer getRdId() {
       return this.rdId;
   }

   /**
    * 设置数据ID
    */
   public void setRdValue(Integer rdValue) {
       this.rdValue = rdValue;
   }

   /**
    * 获取数据ID
    */
   public Integer getRdValue() {
       return this.rdValue;
   }

   /**
    * 设置用户ID
    */
   public void setRdUserId(Integer rdUserId) {
       this.rdUserId = rdUserId;
   }

   /**
    * 获取用户ID
    */
   public Integer getRdUserId() {
       return this.rdUserId;
   }

   /**
    * 设置表名
    */
   public void setRdTable(String rdTable) {
       this.rdTable = rdTable;
   }

   /**
    * 获取表名
    */
   public String getRdTable() {
       return this.rdTable;
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
    * 设置更新时间
    */
   public void setCreateDate(Date createDate) {
       this.createDate = createDate;
   }

   /**
    * 获取更新时间
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
    * 设置表对应列名
    */
   public void setRdColumn(String rdColumn) {
       this.rdColumn = rdColumn;
   }

   /**
    * 获取表对应列名
    */
   public String getRdColumn() {
       return this.rdColumn;
   }

   /**
    * 设置对应模块编码
    */
   public void setRdModel(String rdModel) {
       this.rdModel = rdModel;
   }

   /**
    * 获取对应模块编码
    */
   public String getRdModel() {
       return this.rdModel;
   }

   /**
    * 设置未删除：0:未删除，1：已删除
    */
   public void setRdDel(Integer rdDel) {
       this.rdDel = rdDel;
   }

   /**
    * 获取未删除：0:未删除，1：已删除
    */
   public Integer getRdDel() {
       return this.rdDel;
   }

   public String getUserIds() {
       return userIds;
   }

   public void setUserIds(String userIds) {
       this.userIds = userIds;
   }

   public Integer getProcess() {
       return process;
   }

   public void setProcess(Integer process) {
       this.process = process;
   }

   public String getQuerySql() {
       return querySql;
   }

   public void setQuerySql(String querySql) {
       this.querySql = querySql;
   }

   public String getRdContent() {
       return rdContent;
   }

   public void setRdContent(String rdContent) {
       this.rdContent = rdContent;
   }

   public String getRdCodeOrNames() {
       return rdCodeOrNames;
   }

   public void setRdCodeOrNames(String rdCodeOrNames) {
       this.rdCodeOrNames = rdCodeOrNames;
   }

}