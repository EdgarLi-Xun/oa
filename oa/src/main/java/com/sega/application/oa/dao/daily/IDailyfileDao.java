package com.sega.application.oa.dao.daily;

import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.entity.daily.DailyfileEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 持久层
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2020-3-4 17:09:48<br/>
 * 历史修订：<br/>
 */
@Mapper
public interface IDailyfileDao extends IBaseDao {
  DailyfileEntity querydailyfile();

  void deleteByDailyid(@Param("dailyId") Long dailyId);

  List<DailyfileEntity> getByDailyid(@Param("dailyId") Long dailyId);
}
