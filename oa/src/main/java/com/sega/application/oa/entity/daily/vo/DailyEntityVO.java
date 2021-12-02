package com.sega.application.oa.entity.daily.vo;

import com.sega.application.oa.entity.daily.DailyDetailEntity;
import com.sega.application.oa.entity.daily.DailyEntity;
import com.sega.application.oa.entity.daily.DailyHazardEntity;
import com.sega.application.oa.entity.daily.DailyPlanEntity;
import lombok.Data;

import java.util.List;

/**
 * 类功能说明:
 * 类修改者	创建日期2020/10/29
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 * @description 说明：
 **/
@Data
public class DailyEntityVO {

  private DailyEntity daily;

  /**
   * 风险
   */
  private List<DailyHazardEntity> dailyHazard;

  /**
   * 计划
   */
  private List<DailyPlanEntity> dailyPlan;

  /**
   * 工作内容
   */
  private List<DailyDetailEntity> dailyDetail;

  /**
   * 是否需要发送
   */
  private boolean sende;
}
