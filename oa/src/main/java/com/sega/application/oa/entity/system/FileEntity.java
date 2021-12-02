package com.sega.application.oa.entity.system;

import java.util.Date;


/**
 * 实体
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:22:36<br/>
 * 历史修订：<br/>
 */
public class FileEntity extends BaseEntity {
	
	/**
	 * 文件ID
	 */
	private Long fileId;
	
	/**
	 * 文件名称（自动生成）
	 */
	private String fileName;
	
	/**
	 * 系统自动生成的流水号文件名称
	 */
	private String fileRealName;
	
	/**
	 * 文件类型
	 */
	private String fileType;
	
	/**
	 * 文件路径
	 */
	private String filePath;
	
	/**
	 * 创建人
	 */
	private Long fileCreateBy;
	
	/**
	 * 创建时间
	 */
	private Date fileCreateDate;
	
	/**
	 * 更新人
	 */
	private Long fileUpdateBy;
	
	/**
	 * 更新时间
	 */
	private Date fileUpdateDate;
	
	/**
	 * 是否删除：0：未删除，1：已删除
	 */
	private Integer fileDel;
	
	public FileEntity(){}
	
	/**
	 * 设置文件ID
	 */
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	/**
	 * 获取文件ID
	 */
	public Long getFileId() {
		return this.fileId;
	}
	
	/**
	 * 设置文件名称（自动生成）
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 获取文件名称（自动生成）
	 */
	public String getFileName() {
		return this.fileName;
	}
	
	/**
	 * 设置系统自动生成的流水号文件名称
	 */
	public void setFileRealName(String fileRealName) {
		this.fileRealName = fileRealName;
	}

	/**
	 * 获取系统自动生成的流水号文件名称
	 */
	public String getFileRealName() {
		return this.fileRealName;
	}
	
	/**
	 * 设置文件类型
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * 获取文件类型
	 */
	public String getFileType() {
		return this.fileType;
	}
	
	/**
	 * 设置文件路径
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * 获取文件路径
	 */
	public String getFilePath() {
		return this.filePath;
	}
	
	/**
	 * 设置创建人
	 */
	public void setFileCreateBy(Long fileCreateBy) {
		this.fileCreateBy = fileCreateBy;
	}

	/**
	 * 获取创建人
	 */
	public Long getFileCreateBy() {
		return this.fileCreateBy;
	}
	
	/**
	 * 设置创建时间
	 */
	public void setFileCreateDate(Date fileCreateDate) {
		this.fileCreateDate = fileCreateDate;
	}

	/**
	 * 获取创建时间
	 */
	public Date getFileCreateDate() {
		return this.fileCreateDate;
	}
	
	/**
	 * 设置更新人
	 */
	public void setFileUpdateBy(Long fileUpdateBy) {
		this.fileUpdateBy = fileUpdateBy;
	}

	/**
	 * 获取更新人
	 */
	public Long getFileUpdateBy() {
		return this.fileUpdateBy;
	}
	
	/**
	 * 设置更新时间
	 */
	public void setFileUpdateDate(Date fileUpdateDate) {
		this.fileUpdateDate = fileUpdateDate;
	}

	/**
	 * 获取更新时间
	 */
	public Date getFileUpdateDate() {
		return this.fileUpdateDate;
	}
	
	/**
	 * 设置是否删除：0：未删除，1：已删除
	 */
	public void setFileDel(Integer fileDel) {
		this.fileDel = fileDel;
	}

	/**
	 * 获取是否删除：0：未删除，1：已删除
	 */
	public Integer getFileDel() {
		return this.fileDel;
	}
	
}