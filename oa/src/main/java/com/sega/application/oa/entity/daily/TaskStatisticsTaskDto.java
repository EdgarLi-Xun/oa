package com.sega.application.oa.entity.daily;

import com.sega.application.oa.entity.system.BaseEntity;
import lombok.Data;

/**
 * Descirption:	工时统计台账Dto
 *
 * @create 2021-02-01 10:15
 * @auther: dsl
 */
@Data
public class TaskStatisticsTaskDto extends BaseEntity {
  /**
   * 工时总时
   */
  private Double spendSum;
  /**
   * 项目名称
   */
  private String projectName;
  /**
   * 任务类型
   */
  private String taskType;
}
