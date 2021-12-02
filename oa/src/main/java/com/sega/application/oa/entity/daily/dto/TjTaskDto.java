package com.sega.application.oa.entity.daily.dto;

import com.sega.application.oa.entity.system.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 统计工时返回
 */
@Data
@Accessors(chain = true)
public class TjTaskDto extends BaseEntity {

  /**
   * 项目id
   */
  private String projectId;
  /**
   * 项目名称
   */
  private String projectName;
  /**
   * 用户名称
   */
  private String userName;
  /**
   * 任务类型
   */
  private String taskType;
  /**
   * 工时合
   */
  private Double sumTask;
  /**
   * 用户id
   */
  private String userId;

  /**
   * 需求工时
   */
  private Double demandTime;

  /**
   * 开发工时
   */
  private Double develTime;

  /**
   * 测试工时
   */
  private Double testTime;

  /**
   * 实施工时
   */
  private Double ImplementTime;

  /**
   * 其他工时
   */
  private Double otherTime;
}
