package com.sega.application.oa.entity.project;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sega.application.oa.entity.system.BaseEntity;
import lombok.Data;


/**
 * 实体
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2019-12-12 11:44:56<br/>
 * 历史修订：<br/>
 */
@Data
public class TaskTimeEntity extends BaseEntity {

  /**
   * 任务耗时
   */
  private Long ttId;

  /**
   * 日期
   */
  private Date ttDate;

  /**
   * 日期
   */
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date ttDateStart;

  /**
   * 日期
   */
  @JsonFormat(pattern ="yyyy-MM-dd")
  private Date ttDateEnd;

  /**
   * 用户Id
   */
  private Long ttUserId;

  /**
   * 用户名称
   */
  private String ttUserName;

  /**
   * 完成进度
   */
  private Double ttRate;

  /**
   * 耗时
   */
  private Double ttSpend;

  /**
   * 项目id
   */
  private Long ttProjectId;

  /**
   * 项目名称
   */
  private String ttProjectName;

  /**
   * 百分比
   */
  private Double ttPercentage;

  /**
   * 具体任务
   */
  private String ttTypeDetail;

  /**
   * 项目类型：0：项目任务，1：非项目任务
   */
  private String ttType;

  /**
   * 创建人
   */
  private Long ttCreateBy;

  /**
   * 创建时间
   */
  private Date ttCreateDate;

  /**
   *
   */
  private Long ttUpdateBy;

  /**
   *
   */
  private Date ttUpdateDate;

  /**
   * 是否删除：0，未删除，1：已删除
   */
  private Integer ttDel;

  /**
   * 1需求、2开发、3测试、4实施、5其他
   */
  private String ttTaskType;
  /**
   * 计划工时
   */
  private Double ttPlanSpend;
  /**
   * 备注
   */
  private String ttRemarks;

  /**
   * 审核状态
   */
  private String ttState;

  /**
   * 审核意见
   */
  private String ttReview;
}
