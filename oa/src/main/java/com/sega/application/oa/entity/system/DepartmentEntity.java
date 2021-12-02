package com.sega.application.oa.entity.system;

import java.util.Date;
import java.util.List;

/**
* 实体
* @author sunqian
* @version
* 版本号：1.0.0<br/>
* 创建日期：2019-8-2 16:00:18<br/>
* 历史修订：<br/>
*/
public class DepartmentEntity extends BaseEntity {

   /**
    * 父级数据
    */
   private DepartmentEntity parentDepartment;

   /**
    * 部门所属公司信息
    */
   private DepartmentEntity company;

    /**
     * Id
     * 用户显示树状结构
     */
   private Long id;

    /**
     * title
     * 用户显示树状结构
     */
   private String title;

    /**
     * title
     * 用户显示树状结构
     */
    private String label;

    /**
     * 子部门
     */
   private List<DepartmentEntity> children;
   /**
    * 公司或部门编号主键自维护
    */
   private Long departmentId;
   /**
    * 部门所属公司ID
    */
   private Long departmentCompanyId;
   /**
    * 公司部门编号
    */
   private String departmentCode;
   /**
    * 公司部门名称
    */
   private String departmentName;
   /**
    * 公司邮箱
    */
   private String departmentEmail;
   /**
    * 邮编编码
    */
   private String departmentPostcode;
   /**
    * 公司传真
    */
   private String departmentFax;
   /**
    * 公司电话
    */
   private String departmentTelephone;
   /**
    * 公司部门类型
       0：集团公司
       1：厂
       2：科室
    */
   private Integer departmentType;
   /**
    * 上级部门编号
    */
   private Long departmentParentId;
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
    * 公司介绍
    */
   private String departmentIntroduction;
   /**
    * 公司地址
    */
   private String departmentAddress;
   /**
    * 是否已删除：0：未删除，1已删除
    */
   private Integer departmentDel;

  private boolean expand ;

  public boolean isExpand() {
    return expand;
  }

  public void setExpand(boolean expand) {
    this.expand = expand;
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  private boolean disabled ;
   /**
    * 查询sql
    */
   private String querySql;

   public DepartmentEntity(){}

   /**
    * 设置公司或部门编号主键自维护
    */
   public void setDepartmentId(Long departmentId) {
       this.departmentId = departmentId;
   }

   /**
    * 获取公司或部门编号主键自维护
    */
   public Long getDepartmentId() {
       this.id = this.departmentId;
       return this.departmentId;
   }

   /**
    * 设置公司部门编号
    */
   public void setDepartmentCode(String departmentCode) {
       this.departmentCode = departmentCode;
   }

   /**
    * 获取公司部门编号
    */
   public String getDepartmentCode() {
       return this.departmentCode;
   }

   /**
    * 设置公司部门名称
    */
   public void setDepartmentName(String departmentName) {
       this.title = departmentName;
       this.label = departmentName;
       this.departmentName = departmentName;
   }

   /**
    * 获取公司部门名称
    */
   public String getDepartmentName() {
       return this.departmentName;
   }

   /**
    * 设置公司邮箱
    */
   public void setDepartmentEmail(String departmentEmail) {
       this.departmentEmail = departmentEmail;
   }

   /**
    * 获取公司邮箱
    */
   public String getDepartmentEmail() {
       return this.departmentEmail;
   }

   /**
    * 设置邮编编码
    */
   public void setDepartmentPostcode(String departmentPostcode) {
       this.departmentPostcode = departmentPostcode;
   }

   /**
    * 获取邮编编码
    */
   public String getDepartmentPostcode() {
       return this.departmentPostcode;
   }

   /**
    * 设置公司传真
    */
   public void setDepartmentFax(String departmentFax) {
       this.departmentFax = departmentFax;
   }

   /**
    * 获取公司传真
    */
   public String getDepartmentFax() {
       return this.departmentFax;
   }

   /**
    * 设置公司电话
    */
   public void setDepartmentTelephone(String departmentTelephone) {
       this.departmentTelephone = departmentTelephone;
   }

   /**
    * 获取公司电话
    */
   public String getDepartmentTelephone() {
       return this.departmentTelephone;
   }

   /**
    * 设置公司部门类型
0：集团公司
1：厂
2：科室
    */
   public void setDepartmentType(Integer departmentType) {
       this.departmentType = departmentType;
   }

   /**
    * 获取公司部门类型
0：集团公司
1：厂
2：科室
    */
   public Integer getDepartmentType() {
       return this.departmentType;
   }

   /**
    * 设置上级部门编号
    */
   public void setDepartmentParentId(Long departmentParentId) {
       this.departmentParentId = departmentParentId;
   }

   /**
    * 获取上级部门编号
    */
   public Long getDepartmentParentId() {
       return this.departmentParentId;
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
    * 设置公司介绍
    */
   public void setDepartmentIntroduction(String departmentIntroduction) {
       this.departmentIntroduction = departmentIntroduction;
   }

   /**
    * 获取公司介绍
    */
   public String getDepartmentIntroduction() {
       return this.departmentIntroduction;
   }

   /**
    * 设置公司地址
    */
   public void setDepartmentAddress(String departmentAddress) {
       this.departmentAddress = departmentAddress;
   }

   /**
    * 获取公司地址
    */
   public String getDepartmentAddress() {
       return this.departmentAddress;
   }

   /**
    * 设置是否已删除：0：未删除，1已删除
    */
   public void setDepartmentDel(Integer departmentDel) {
       this.departmentDel = departmentDel;
   }

   /**
    * 获取是否已删除：0：未删除，1已删除
    */
   public Integer getDepartmentDel() {
       return this.departmentDel;
   }

   public DepartmentEntity getParentDepartment() {
       return parentDepartment;
   }

   public void setParentDepartment(DepartmentEntity parentDepartment) {
       this.parentDepartment = parentDepartment;
   }
   public DepartmentEntity getCompany() {
       return company;
   }

   public void setCompany(DepartmentEntity company) {
       this.company = company;
   }

   public Long getDepartmentCompanyId() {
       return departmentCompanyId;
   }

   public void setDepartmentCompanyId(Long departmentCompanyId) {
       this.departmentCompanyId = departmentCompanyId;
   }

   public String getQuerySql() {
       return querySql;
   }

   public void setQuerySql(String querySql) {
       this.querySql = querySql;
   }

    public List<DepartmentEntity> getChildren() {
        return children;
    }

    public void setChildren(List<DepartmentEntity> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
