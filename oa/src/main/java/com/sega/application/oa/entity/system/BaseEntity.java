package com.sega.application.oa.entity.system;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class BaseEntity implements Serializable {

	/**
	 *	分页数
	 */
	private Integer pageNum;

	/**
	 * 每页数量
	 */
	private Integer pageSize;

	/**
	 * id字符串集合
	 */
	private String ids;

	/**
	 * 目标实体id
	 */
	private Integer destinationId;

	/**
	 * id数组集合
	 */
	@JSONField(serialize=false)
	private String[] idArray;

	@JSONField(serialize=false)
	private Integer[] departmentIdArray;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date launchDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date acceptanceDate;

  @JSONField(serialize=false)
	private Long createBy;

	@JSONField(serialize=false)
	private Long updateBy;

	private Date createDate;

	private Date updateDate;

	@JSONField(serialize=false)
	protected String sqlWhere;

	/**
	 * 数据权限sql
	 */
	@JSONField(serialize=false)
	protected String dataSql;

	/**
	 * 批量导入的数据权限
	 */
	@JSONField(serialize=false)
	protected String departmentIds;

	/**
	 *  排序字段名称
	 */
	@JSONField(serialize=false)
	protected String sort;

	/**
	 *  排序方式
	 */
	@JSONField(serialize=false)
	protected String order = "desc";

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		StringBuffer sqlString = new StringBuffer();
		if (sort != null) {
			for (int i = 0; i < sort.length(); i++) {
				char c = sort.charAt(i);
				if (Character.isUpperCase(c)) {
					sqlString.append("_" + Character.toLowerCase(c));
				} else if (Character.isLowerCase(c)) {
					sqlString.append(c);
				}
			}
		}
		this.sort = sqlString.toString();
	}

  public Date getLaunchDate() {
    return launchDate;
  }

  public void setLaunchDate(Date launchDate) {
    this.launchDate = launchDate;
  }

  public Date getAcceptanceDate() {
    return acceptanceDate;
  }

  public void setAcceptanceDate(Date acceptanceDate) {
    this.acceptanceDate = acceptanceDate;
  }

  public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getSqlWhere() {
		return sqlWhere;
	}

	public void setSqlWhere(String sqlWhere) {
		this.sqlWhere = sqlWhere;
	}

	public String[] getIdArray() {
		return idArray;
	}

	public void setIdArray(String[] idArray) {
		this.idArray = idArray;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		if (!StringUtils.isEmpty(ids)) {
			String[] idArray = ids.split(",");
			this.idArray = idArray;
		}
		this.ids = ids;
	}

	public String getDataSql() {
		return dataSql;
	}

	public void setDataSql(String dataSql) {
		this.dataSql = dataSql;
	}

	public Integer[] getDepartmentIdArray() {
		return departmentIdArray;
	}

	public void setDepartmentIdArray(Integer[] departmentIdArray) {
		this.departmentIdArray = departmentIdArray;
	}

	public String getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(String departmentIds) {
		this.departmentIds = departmentIds;
	}

	public Integer getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(Integer destinationId) {
		this.destinationId = destinationId;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
