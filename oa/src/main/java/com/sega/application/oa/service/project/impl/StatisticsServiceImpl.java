package com.sega.application.oa.service.project.impl;

import com.sega.application.oa.dao.IStatisticsDao;
import com.sega.application.oa.dao.ITaskTimeDao;
import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.entity.daily.dto.MailDto;
import com.sega.application.oa.entity.daily.dto.TjTaskDto;
import com.sega.application.oa.service.project.IStatisticsService;
import com.sega.application.oa.service.system.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsServiceImpl extends BaseServiceImpl implements IStatisticsService {

  @Resource
  private IStatisticsDao statisticsDao;

  @Override
  protected IBaseDao getDao() {
    return null;
  }

  @Override
  public List<TjTaskDto> getTjxmzTaskTime(Date beforeDate,Date nowDate, List<TjTaskDto> list) {
    // 对拿到的结果进行遍历，一个一个的去查。
    ArrayList<TjTaskDto> tjTaskDtos = new ArrayList<>();
    for (TjTaskDto tjTaskDto : list) {
      TjTaskDto tjTaskDto1  =  statisticsDao.queryxmSr(beforeDate,nowDate,tjTaskDto);
      tjTaskDtos.add(tjTaskDto1);
    }
    return tjTaskDtos;
  }

  @Override
  public List<TjTaskDto> getTjygzTaskTime(Date beforeDate,Date nowDate, List<TjTaskDto> list) {
    // 对拿到的结果进行遍历，一个一个的去查。
    ArrayList<TjTaskDto> tjTaskDtos = new ArrayList<>();
    for (TjTaskDto tjTaskDto : list) {
      List<TjTaskDto> ygtjList = statisticsDao.queryygSr(beforeDate, nowDate, tjTaskDto);
      for (TjTaskDto taskDto : ygtjList) {
        tjTaskDtos.add(taskDto);
      }
    }
    return tjTaskDtos;
  }

  @Override
  public List<TjTaskDto> getObjectId() {
    return statisticsDao.getObjectId();
  }

  @Override
  public List<TjTaskDto> getUserId(Integer num) {
    return statisticsDao.getUserId(num);
  }

  @Override
  public String getSjr(int depId) {
    return statisticsDao.getSjr(depId);
  }


}
