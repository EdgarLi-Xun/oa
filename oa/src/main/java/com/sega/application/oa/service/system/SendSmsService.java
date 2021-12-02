package com.sega.application.oa.service.system;


public interface SendSmsService {

  /**
   * 发送短信的接口
   *
   * @param phoneNum 手机号
   * @param message     消息
   * @return
   */
  boolean sendSms(String phoneNum, String message);
}
