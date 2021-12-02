package com.sega.application.oa.entity.daily.dto;


import com.sega.application.oa.entity.system.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 统计工时返回
 */
@Data
@Accessors(chain = true)
public class MailDto extends BaseEntity {

  /**
   * 链接协议
   */

  private String ljxy;

  /**
   * 主机名
   */

  private String zjm;

  /**
   * 端口号
   */

  private Integer dkh;

  /**
   * 发件人邮箱地址
   */

  private String fjryxdz;


  /**
   * 授权码
   */

  private String sqm;


}
