package com.sega.application.oa.entity.daily;

import java.util.Date;

import com.sega.application.oa.entity.system.BaseEntity;
import lombok.Data;


/**
 * 实体
 * @author 孙乾
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:22:36<br/>
 * 历史修订：<br/>
 */
 @Data
public class DailyDetailEntity extends BaseEntity {

	/**
	 *
	 */
	private Long ddId;

	/**
	 * 日报id
	 */
	private Long ddDailyId;

	/**
	 * 项目id
	 */
	private Long ddProjectId;

	/**
	 * 项目名称
	 */
	private String ddProjectName;

	/**
	 * 耗时
	 */
	private String ddTime;

	/**
	 * 工作内容
	 */
	private String ddContent;

	/**
	 * 进度
	 */
	private String ddSchedule;

	/**
	 * 备注
	 */
	private String ddRemarks;

	/**
	 * 创建人
	 */
	private Long ddCreateBy;

	/**
	 * 创建时间
	 */
	private Date ddCreateDate;

	/**
	 * 修改人
	 */
	private Long ddUpdateBy;

	/**
	 * 更新时间
	 */
	private Date ddUpdateDate;

	/**
	 * 是否删除，0:未删除，1：已删除
	 */
	private Integer ddDel;


}
