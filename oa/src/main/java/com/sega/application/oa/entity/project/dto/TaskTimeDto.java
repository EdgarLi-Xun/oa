package com.sega.application.oa.entity.project.dto;

import com.sega.application.oa.entity.project.TaskTimeEntity;
import lombok.Data;

/**
 * @description: 工时统计
 * @author: EdgarLi
 * @date: 2021-10-18 15:21
 **/
@Data
public class TaskTimeDto extends TaskTimeEntity {
  /**
   * 人/天
   */
  private Double spendDay = 0.0;
  /**
   * 人/天
   */
  private String txDate ;
}
