package com.sega.application.oa.entity.system;

import com.sega.application.oa.annotation.Table;
import lombok.Data;

@Data
@Table(value = "mailbox")
public class MailBox {
  /**
   * id
   */
  private Long mailId;

  /**
   * 类型
   */
  private Integer type;

  /**m
   * 发件人邮箱
   */

  private Long fsr;

  /**
   * 接收人邮箱
   */

  private String jsr;

  /**
   * 接受组对应部门id
   */

  private Long jsz;

  /**
   * 抄送人邮箱
   */

  private String csr;

  /**
   * 抄送组对应部门id
   */

  private Long csz;

  /**
   * 是否删除
   */

  private Integer mailDel;

  /**
   * 备注
   */

  private String remarks;

}
