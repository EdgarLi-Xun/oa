package com.sega.application.oa.entity.daily;

import com.sega.application.oa.entity.system.BaseEntity;
import lombok.Data;

/**
 * 统计工时Dto:
 *
 * @create 2021-02-04 10:18
 * @auther: dsl
 */
@Data
public class TjTaskTimeDto extends BaseEntity {
  /**
   * 开发工时
   */
  private Double develTime;

  /**
   * 需求工时
   */
  private Double demandTime;
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
