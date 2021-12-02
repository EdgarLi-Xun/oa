package com.sega.application.oa.service.project;

import com.sega.application.oa.entity.daily.dto.TjTaskDto;
import com.sega.application.oa.service.system.IBaseService;

import java.sql.Date;
import java.util.List;

public interface IStatisticsService extends IBaseService {

  // 统计项目一周工时结果
  List<TjTaskDto> getTjxmzTaskTime(Date beforeDate, Date nowDate, List<TjTaskDto> list);

  // 统计员工一周工时结果
  List<TjTaskDto> getTjygzTaskTime(Date beforeDate,Date nowDate, List<TjTaskDto> list);

  List<TjTaskDto> getObjectId();

  List<TjTaskDto> getUserId(Integer num);


  String getSjr(int i);
}
