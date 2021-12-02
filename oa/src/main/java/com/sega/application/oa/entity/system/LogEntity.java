package com.sega.application.oa.entity.system;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 实体
 * 
 * @author sunqian
 * @version 版本号：1.0.0<br/>
 *          创建日期：2019-8-5 20:50:59<br/>
 *          历史修订：<br/>
 */
public class LogEntity extends BaseEntity {

	/**
	 * 日志主键
	 */
	private Integer logId;
	
	/**
	 * 关联部门
	 */
	private DepartmentEntity department;
	
	/**
	 * 关联用户
	 */
	private UserEntity user;
	
	/**
	 * 操作用户id
	 */
	private Integer logUserId;
	
	/**
	 * 操作用户所属公司名称
	 */
	private Integer logDepartmentId;
	
	/**
	 * 操作模块
	 */
	private String logModule;
	
	/**
	 * 模块编号
	 */
	private String logModelCode;
	
	/**
	 * 操作表名
	 */
	private String logTable;
	
	/**
	 * 记录日志的记录主键
	 */
	private Integer logBusinessKey;
	
	/**
	 * 操作方法
	 */
	private String logOperation;
	
	/**
	 * 操作内容
	 */
	private String logContent;
	
	/**
	 * 操作数据
	 */
	private String logValue;
	
	/**
	 * 操作时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date logDate;
	
	/**
	 * 统计数量
	 */
	private String logCount;
	
	/**
	 * 模块标识
	 */
	private String logModuleSign;

	public LogEntity() {
	}

	/**
	 * 设置
	 */
	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	/**
	 * 获取
	 */
	public Integer getLogId() {
		return this.logId;
	}

	/**
	 * 设置操作用户id
	 */
	public void setLogUserId(Integer logUserId) {
		this.logUserId = logUserId;
	}

	/**
	 * 获取操作用户id
	 */
	public Integer getLogUserId() {
		return this.logUserId;
	}

	/**
	 * 设置操作模块
	 */
	public void setLogModule(String logModule) {
		this.logModule = logModule;
	}

	/**
	 * 获取操作模块
	 */
	public String getLogModule() {
		return this.logModule;
	}

	/**
	 * 设置操作表名
	 */
	public void setLogTable(String logTable) {
		this.logTable = logTable;
	}

	/**
	 * 获取操作表名
	 */
	public String getLogTable() {
		return this.logTable;
	}

	/**
	 * 设置操作方法
	 */
	public void setLogOperation(String logOperation) {
		this.logOperation = logOperation;
	}

	/**
	 * 获取操作方法
	 */
	public String getLogOperation() {
		return this.logOperation;
	}

	/**
	 * 设置操作内容
	 */
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	/**
	 * 获取操作内容
	 */
	public String getLogContent() {
		return this.logContent;
	}

	/**
	 * 设置操作时间
	 */
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	/**
	 * 获取操作时间
	 */
	public Date getLogDate() {
		return this.logDate;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Integer getLogBusinessKey() {
		return logBusinessKey;
	}

	public void setLogBusinessKey(Integer logBusinessKey) {
		this.logBusinessKey = logBusinessKey;
	}

	public String getLogValue() {
		return logValue;
	}

	public void setLogValue(String logValue) {
		this.logValue = logValue;
	}

	public Integer getLogDepartmentId() {
		return logDepartmentId;
	}

	public void setLogDepartmentId(Integer logDepartmentId) {
		this.logDepartmentId = logDepartmentId;
	}

	public String getLogModelCode() {
		return logModelCode;
	}

	public void setLogModelCode(String logModelCode) {
		this.logModelCode = logModelCode;
	}

	public String getLogCount() {
		return logCount;
	}

	public void setLogCount(String logCount) {
		this.logCount = logCount;
	}

	public DepartmentEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentEntity department) {
		this.department = department;
	}

	public String getLogModuleSign() {
		return logModuleSign;
	}

	public void setLogModuleSign(String logModuleSign) {
		this.logModuleSign = logModuleSign;
	}

}