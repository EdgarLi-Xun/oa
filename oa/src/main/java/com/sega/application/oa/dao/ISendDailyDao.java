package com.sega.application.oa.dao;

import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.entity.daily.DailyEntity;
import com.sega.application.oa.entity.system.MailBox;
import com.sega.application.oa.entity.system.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Mapper
public interface ISendDailyDao extends IBaseDao {

  /**
   * 查询需要发送日报员工的id集合
   *
   * @return
   */
  List<UserEntity> getUserId();


  Set<String> getUserName(Date nowDate);

  String getName(Long userId);

  List<String> getEmail(Long jsz);

  String getFsr(Integer type);

  String getPassword(String fsr);

  MailBox getJsr(int jsr);
}
