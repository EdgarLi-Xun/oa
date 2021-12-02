package com.sega.application.oa.entity.humanEngineering;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.sega.application.oa.entity.system.BaseEntity;


 /**
 * 实体
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2020-1-8 15:57:37<br/>
 * 历史修订：<br/>
 */
public class AttendanceRecordEntity extends BaseEntity {
	
	/**
	 * 加班审核id
	 */
	private Long arId;
	
	/**
	 * 上班人名称
	 */
	private String arUsername;
	
	/**
	 * 上班人id
	 */
	private Long arUserid;
	
	/**
	 * 标记：0：无异常，1：异常
	 */
	private Integer arSymbol;
	
	/**
	 * 异常状态
	 */
	private String arDebuff;
	
	/**
	 * 审核人名称
	 */
	private String arCheckerName;
	
	/**
	 * 审核人id
	 */
	private Long arCheckerId;
	
	/**
	 * 加班状态：0：工作日加班，1：周末半天班，2：周末全天班，3：未加班
	 */
	private Integer arOverstate;
	
	/**
	 * 备注
	 */
	private String arRemark;
	
	/**
	 * 日期
	 */
	private Date arDate;
	
	/**
	 * 星期
	 */
	private String arWeek;
	
	/**
	 * 上班时长
	 */
	private Double arTime;
	
	/**
	 * 工资调休日：0：无调休无工资，1：有调休无工资，2：无调休有工资
	 */
	private Integer arLieupayState;

	 /**
	  * 获得工资调休日
	  */
	 private Double arGetLieupay;
	
	/**
	 * 金额：餐补和迟到
	 */
	private Integer arMoney;
	
	/**
	 * 原始数据
	 */
	private String arBasicdata;
	
	/**
	 * 创建人
	 */
	private Long arCreateBy;
	
	/**
	 * 创建时间
	 */
	private Date arCreateDate;
	
	/**
	 * 更新人
	 */
	private Long arUpdateBy;
	
	/**
	 * 更新时间
	 */
	private Date arUpdateDate;
	
	/**
	 * 是否删除：0：未删除，1：已删除
	 */
	private Integer arDel;
	
	public AttendanceRecordEntity(){}
	
	/**
	 * 设置加班审核id
	 */
	public void setArId(Long arId) {
		this.arId = arId;
	}

	/**
	 * 获取加班审核id
	 */
	public Long getArId() {
		return this.arId;
	}
	
	/**
	 * 设置上班人名称
	 */
	public void setArUsername(String arUsername) {
		this.arUsername = arUsername;
	}

	/**
	 * 获取上班人名称
	 */
	public String getArUsername() {
		return this.arUsername;
	}
	
	/**
	 * 设置上班人id
	 */
	public void setArUserid(Long arUserid) {
		this.arUserid = arUserid;
	}

	/**
	 * 获取上班人id
	 */
	public Long getArUserid() {
		return this.arUserid;
	}
	
	/**
	 * 设置标记：0：无异常，1：异常
	 */
	public void setArSymbol(Integer arSymbol) {
		this.arSymbol = arSymbol;
	}

	/**
	 * 获取标记：0：无异常，1：异常
	 */
	public Integer getArSymbol() {
		return this.arSymbol;
	}
	
	/**
	 * 设置异常状态
	 */
	public void setArDebuff(String arDebuff) {
		this.arDebuff = arDebuff;
	}

	/**
	 * 获取异常状态
	 */
	public String getArDebuff() {
		return this.arDebuff;
	}
	
	/**
	 * 设置审核人名称
	 */
	public void setArCheckerName(String arCheckerName) {
		this.arCheckerName = arCheckerName;
	}

	/**
	 * 获取审核人名称
	 */
	public String getArCheckerName() {
		return this.arCheckerName;
	}
	
	/**
	 * 设置审核人id
	 */
	public void setArCheckerId(Long arCheckerId) {
		this.arCheckerId = arCheckerId;
	}

	/**
	 * 获取审核人id
	 */
	public Long getArCheckerId() {
		return this.arCheckerId;
	}
	
	/**
	 * 设置加班状态：0：工作日加班，1：周末半天班，2：周末全天班，3：未加班
	 */
	public void setArOverstate(Integer arOverstate) {
		this.arOverstate = arOverstate;
	}

	/**
	 * 获取加班状态：0：工作日加班，1：周末半天班，2：周末全天班，3：未加班
	 */
	public Integer getArOverstate() {
		return this.arOverstate;
	}
	
	/**
	 * 设置备注
	 */
	public void setArRemark(String arRemark) {
		this.arRemark = arRemark;
	}

	/**
	 * 获取备注
	 */
	public String getArRemark() {
		return this.arRemark;
	}
	
	/**
	 * 设置日期
	 */
	public void setArDate(Date arDate) {
		this.arDate = arDate;
	}

	/**
	 * 获取日期
	 */
	public Date getArDate() {
		return this.arDate;
	}
	
	/**
	 * 设置星期
	 */
	public void setArWeek(String arWeek) {
		this.arWeek = arWeek;
	}

	/**
	 * 获取星期
	 */
	public String getArWeek() {
		return this.arWeek;
	}
	
	/**
	 * 设置上班时长
	 */
	public void setArTime(Double arTime) {
		this.arTime = arTime;
	}

	/**
	 * 获取上班时长
	 */
	public Double getArTime() {
		return this.arTime;
	}
	
	/**
	 * 设置工资调休日：0：无调休无工资，1：有调休无工资，2：无调休有工资
	 */
	public void setArLieupayState(Integer arLieupayState) {
		this.arLieupayState = arLieupayState;
	}

	/**
	 * 获取工资调休日：0：无调休无工资，1：有调休无工资，2：无调休有工资
	 */
	public Integer getArLieupayState() {
		return this.arLieupayState;
	}

	 public Double getArGetLieupay() {
		 return arGetLieupay;
	 }

	 public void setArGetLieupay(Double arGetLieupay) {
		 this.arGetLieupay = arGetLieupay;
	 }

	 /**
	 * 设置金额：餐补和迟到
	 */
	public void setArMoney(Integer arMoney) {
		this.arMoney = arMoney;
	}

	/**
	 * 获取金额：餐补和迟到
	 */
	public Integer getArMoney() {
		return this.arMoney;
	}
	
	/**
	 * 设置原始数据
	 */
	public void setArBasicdata(String arBasicdata) {
		this.arBasicdata = arBasicdata;
	}

	/**
	 * 获取原始数据
	 */
	public String getArBasicdata() {
		return this.arBasicdata;
	}
	
	/**
	 * 设置创建人
	 */
	public void setArCreateBy(Long arCreateBy) {
		this.arCreateBy = arCreateBy;
	}

	/**
	 * 获取创建人
	 */
	public Long getArCreateBy() {
		return this.arCreateBy;
	}
	
	/**
	 * 设置创建时间
	 */
	public void setArCreateDate(Date arCreateDate) {
		this.arCreateDate = arCreateDate;
	}

	/**
	 * 获取创建时间
	 */
	public Date getArCreateDate() {
		return this.arCreateDate;
	}
	
	/**
	 * 设置更新人
	 */
	public void setArUpdateBy(Long arUpdateBy) {
		this.arUpdateBy = arUpdateBy;
	}

	/**
	 * 获取更新人
	 */
	public Long getArUpdateBy() {
		return this.arUpdateBy;
	}
	
	/**
	 * 设置更新时间
	 */
	public void setArUpdateDate(Date arUpdateDate) {
		this.arUpdateDate = arUpdateDate;
	}

	/**
	 * 获取更新时间
	 */
	public Date getArUpdateDate() {
		return this.arUpdateDate;
	}
	
	/**
	 * 设置是否删除：0：未删除，1：已删除
	 */
	public void setArDel(Integer arDel) {
		this.arDel = arDel;
	}

	/**
	 * 获取是否删除：0：未删除，1：已删除
	 */
	public Integer getArDel() {
		return this.arDel;
	}
	
}