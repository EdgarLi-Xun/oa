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
public class DictEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private Long dictId;
	
	/**
	 * 父级key
	 */
	private String dictPartentKey;
	
	/**
	 * key值，唯一
	 */
	private String dictKey;
	
	/**
	 * 名称
	 */
	private String dictName;
	
	/**
	 * key和name对应的value
	 */
	private String dictValue;
	
	/**
	 * 创建人
	 */
	private Long dictCreateBy;
	
	/**
	 * 创建时间
	 */
	private Date dictCreateDate;
	
	/**
	 * 更新人
	 */
	private Long dictUpdateBy;
	
	/**
	 * 更新时间
	 */
	private Date dictUpdateDate;

	
	/**
	 * 是否删除：0未删除，1已删除
	 */
	private Integer dictDel;
	
	public DictEntity(){}
	
	/**
	 * 设置
	 */
	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}

	/**
	 * 获取
	 */
	public Long getDictId() {
		return this.dictId;
	}
	
	/**
	 * 设置父级key
	 */
	public void setDictPartentKey(String dictPartentKey) {
		this.dictPartentKey = dictPartentKey;
	}

	/**
	 * 获取父级key
	 */
	public String getDictPartentKey() {
		return this.dictPartentKey;
	}
	
	/**
	 * 设置key值，唯一
	 */
	public void setDictKey(String dictKey) {
		this.dictKey = dictKey;
	}

	/**
	 * 获取key值，唯一
	 */
	public String getDictKey() {
		return this.dictKey;
	}
	
	/**
	 * 设置名称
	 */
	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	/**
	 * 获取名称
	 */
	public String getDictName() {
		return this.dictName;
	}
	
	/**
	 * 设置key和name对应的value
	 */
	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

	/**
	 * 获取key和name对应的value
	 */
	public String getDictValue() {
		return this.dictValue;
	}
	
	/**
	 * 设置创建人
	 */
	public void setDictCreateBy(Long dictCreateBy) {
		this.dictCreateBy = dictCreateBy;
	}

	/**
	 * 获取创建人
	 */
	public Long getDictCreateBy() {
		return this.dictCreateBy;
	}
	
	/**
	 * 设置创建时间
	 */
	public void setDictCreateDate(Date dictCreateDate) {
		this.dictCreateDate = dictCreateDate;
	}

	/**
	 * 获取创建时间
	 */
	public Date getDictCreateDate() {
		return this.dictCreateDate;
	}
	
	/**
	 * 设置更新人
	 */
	public void setDictUpdateBy(Long dictUpdateBy) {
		this.dictUpdateBy = dictUpdateBy;
	}

	/**
	 * 获取更新人
	 */
	public Long getDictUpdateBy() {
		return this.dictUpdateBy;
	}
	
	/**
	 * 设置更新时间
	 */
	public void setDictUpdateDate(Date dictUpdateDate) {
		this.dictUpdateDate = dictUpdateDate;
	}

	/**
	 * 获取更新时间
	 */
	public Date getDictUpdateDate() {
		return this.dictUpdateDate;
	}
	
	/**
	 * 设置是否删除：0未删除，1已删除
	 */
	public void setDictDel(Integer dictDel) {
		this.dictDel = dictDel;
	}

	/**
	 * 获取是否删除：0未删除，1已删除
	 */
	public Integer getDictDel() {
		return this.dictDel;
	}
	
}