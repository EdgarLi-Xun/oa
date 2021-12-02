package com.sega.application.oa.service.project;

import com.sega.application.oa.entity.system.MailBox;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.system.IBaseService;

import java.sql.Date;
import java.util.List;
import java.util.Set;

public interface IDailyService extends IBaseService {


  Set<String> getUserName(Date nowDate);

  String getName(Long userId);

  List<String> getEmail(Long jsz);

  String getFsr(Integer type);

  String getPassword(String fsr);

  MailBox getJsr(int jsr);


  /**
   * 每天日报未发送人员
   */
  void dailyStatistics();
}
