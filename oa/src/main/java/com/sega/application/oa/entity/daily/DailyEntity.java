package com.sega.application.oa.entity.daily;

import com.sega.application.oa.entity.system.BaseEntity;
import lombok.Data;

import java.util.Date;


/**
 * 实体
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:22:36<br/>
 * 历史修订：<br/>
 */
@Data
public class DailyEntity extends BaseEntity {

  /**
   * 日报主键自维护
   */
  private Long dailyId;

  /**
   * 发送日期
   */
  private Date dailySendDate;

  /**
   * 发送日期
   */
  private Date dailySendDateTm;

  /**
   * 用户ID
   */
  private Long dayilUserId;

  /**
   * 发送人
   */
  private String dayilUserName;

  /**
   * 日报主题
   */
  private String dailyTheme;

  /**
   * 附件
   */
  private String dailyFile;

  /**
   * 日报发送状态
   */
  private String dailyState;

  /**
   * 日报创建人
   */
  private Long dailyCreateBy;

  /**
   * 日报创建时间
   */
  private Date dailyCreateDate;

  /**
   * 日报更新人
   */
  private Long dailyUpdateBy;

  /**
   * 日报更新时间
   */
  private Date dailyUpdateDate;

  /**
   * 是否删除：0：未删除，1：已删除
   */
  private Integer dailyDel;

  private String dayilsjrName;

  private String[] dayilsjrNameArr;

  private String dayilcsrName;

  private String[] dayilcsrNameArr;

  private Date dailyDate;

  /**
   * 发送标记 0：未发送 1：已发送
   */
  private Integer dailySendTy;

  /**
   * 发送方式：0：pc 1：app 2：微信小程序
   */

  private Integer dailySendBy;
}
