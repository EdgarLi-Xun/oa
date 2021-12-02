package com.sega.application.oa.service.project.impl;

import com.sega.application.oa.entity.daily.dto.TjTaskDto;
import com.sega.application.oa.entity.daily.vo.TjTaskVo;
import com.sega.application.oa.entity.project.ProjectEntity;
import com.sega.application.oa.entity.project.TaskTimeEntity;
import com.sega.application.oa.entity.project.dto.TaskTimeDto;
import com.sega.application.oa.entity.project.vo.TaskTimeVo;
import com.sega.application.oa.entity.system.MailBox;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.project.IDailyService;
import com.sega.application.oa.service.project.IProjectService;
import com.sega.application.oa.service.system.IUserService;
import com.sega.application.oa.service.system.impl.BaseServiceImpl;
import com.sega.application.oa.utils.ReportFormUtil;
import com.sega.application.oa.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sega.application.oa.dao.ITaskTimeDao;
import com.sega.application.oa.service.project.ITaskTimeService;
import com.sega.application.oa.dao.system.IBaseDao;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 管理持久化层
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2019-10-24 17:02:21<br/>
 * 历史修订：<br/>
 */
@Service
@Slf4j
public class TaskTimeServiceImpl extends BaseServiceImpl implements ITaskTimeService {

  @Resource
  private ITaskTimeDao taskTimeDao;
  @Autowired
  private IProjectService projectService;
  @Autowired
  private IUserService userService;
  @Autowired
  private IDailyService dailyService;

  private ITaskTimeService iTaskTimeService;

  @Override
  protected IBaseDao getDao() {
    return taskTimeDao;
  }

  /**
   * 根据当前用户所在部门查询下属工时
   *
   * @param taskTimeEntity
   * @return 集合
   */
  @Override
  public List queryChildrenTask(TaskTimeVo taskTimeEntity) {
    return taskTimeDao.queryChildrenTask(taskTimeEntity);
  }

  /**
   * 统计工时
   *
   * @param tjTaskVo
   * @return
   */
  @Override
  public List<TjTaskVo> getTjTaskTime(TjTaskVo tjTaskVo) {
    List<TjTaskVo> tjTaskTime = new ArrayList<>();
    if (tjTaskVo.getUserId() != null && !"".equals(tjTaskVo.getUserId()) || tjTaskVo.getProjectId() != null && !"".equals(tjTaskVo.getProjectId())) {
      TjTaskVo taskVo = new TjTaskVo();
      TjTaskVo task = new TjTaskVo();
      if (tjTaskVo.getUserId() != null && !"".equals(tjTaskVo.getUserId())) {
        TaskTimeEntity taskTimeEntity = new TaskTimeEntity();
        taskTimeEntity.setTtUserId(Long.parseLong(tjTaskVo.getUserId()));
        List<TaskTimeEntity> query = taskTimeDao.query(taskTimeEntity);
        for (int q = 0; q < query.size(); q++) {
          TjTaskVo task1 = new TjTaskVo();
          task1.setUserId(taskVo.getUserId());
          task1.setProjectId(query.get(q).getTtProjectId() + "");
          taskVo.setUserId(tjTaskVo.getUserId());
          taskVo.setProjectId(query.get(q).getTtProjectId() + "");
          Universal(taskVo, task1);
          task1.setProjectName(query.get(q).getTtProjectName());
          task1.setUserName(query.get(q).getTtUserName());
          tjTaskTime.add(task1);
          for (int i = 0; i < tjTaskTime.size() - 1; i++) {
            for (int j = tjTaskTime.size() - 1; j > i; j--) {
              if (tjTaskTime.get(i).getProjectId().equals(tjTaskTime.get(j).getProjectId())) {
                tjTaskTime.remove(j);
              }
            }
          }
        }
        return tjTaskTime;
      }
      if (tjTaskVo.getProjectId() != null && !"".equals(tjTaskVo.getProjectId())) {
        ProjectEntity projectEntity = projectService.queryByProjectId(tjTaskVo.getProjectId());
        taskVo.setProjectId(projectEntity.getProjectId() + "");
        task.setProjectName(projectEntity.getProjectName());
        Universal(taskVo, task);
        tjTaskTime.add(task);
        return tjTaskTime;
      }
    } else {
      List<ProjectEntity> query = projectService.query(null);
      TjTaskVo taskVo = new TjTaskVo();
      for (int j = 0; j < query.size(); j++) {
        TjTaskVo task = new TjTaskVo();
        taskVo.setProjectId(query.get(j).getProjectId() + "");
        Universal(taskVo, task);
        task.setProjectName(query.get(j).getProjectName());
        tjTaskTime.add(task);
      }
      return tjTaskTime;
    }
    return null;
  }

  @Override
  public List<TjTaskDto> queryByPage(TjTaskVo tjTaskVo) {
    return taskTimeDao.queryByPage(tjTaskVo);
  }

  @Override
  public void gsfsgstjDay() {
    MailBox mailBox = dailyService.getJsr(3);
    UserEntity adminUser = userService.getByUserId(mailBox.getFsr());
    List<TaskTimeEntity> taskTimeLis = taskTimeDao.gsfsgstjDay();
    List<TaskTimeDto> taskTimeDtoList = new ArrayList<>();
    for (TaskTimeEntity taskTime : taskTimeLis) {
      TaskTimeDto taskTimeDto = new TaskTimeDto();
      BeanUtils.copyProperties(taskTime, taskTimeDto);

      if (taskTimeDto.getTtDate() != null) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        taskTimeDto.setTxDate(sdf.format(taskTimeDto.getTtDate()));
      }

      taskTimeDtoList.add(taskTimeDto);
    }

    List<TjTaskDto> xmList = taskTimeDao.gtTjTaskDay();
    // 从数据库读取收件人和收件组,抄送人和抄送组
    List<String> sjrs = new ArrayList<>();
    if (mailBox.getJsz() != null) {
      sjrs = dailyService.getEmail(mailBox.getJsz());
    }
    String jsr = mailBox.getJsr();
    if (!StringUtils.isEmpty(jsr)) {
      sjrs.addAll(Arrays.asList(jsr.split(",")));
    }

    List<String> csrs = new ArrayList<>();
    if (mailBox.getCsz() != null) {
      csrs = dailyService.getEmail(mailBox.getCsz());
    }
    String csr = mailBox.getCsr();
    if (!StringUtils.isEmpty(csr)) {
      csrs.addAll(Arrays.asList(csr.split(",")));
    }
    try {
      ReportFormUtil.projectForm(xmList, changeTaskTime(taskTimeDtoList), adminUser, "每天工时统计发送", sjrs, csrs);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

  }

  private List<TaskTimeDto> changeTaskTime(List<TaskTimeDto> taskTimeDtoList) {
    List<TaskTimeDto> result = new ArrayList<>();
    Map<Long, List<TaskTimeDto>> userMap = Utils.groupMap(taskTimeDtoList, TaskTimeDto::getTtUserId);
    for (Map.Entry<Long, List<TaskTimeDto>> userList : userMap.entrySet()) {
      List<TaskTimeDto> dtos = userList.getValue();
      Double add = dtos.stream().mapToDouble(TaskTimeDto::getTtSpend).sum();
      for (TaskTimeDto dto : dtos) {
        if (add < 8) {
          add = Double.valueOf(8);
        }
        Double value = dto.getTtSpend() / add * 8 / 8;
        dto.setSpendDay(value);
        result.add(dto);
      }
    }
    return result;
  }

  @Override
  public void gsfsgstjWeek() {

    MailBox mailBox = dailyService.getJsr(3);
    UserEntity adminUser = userService.getByUserId(mailBox.getFsr());
    List<TaskTimeEntity> taskTimeLis = taskTimeDao.gsfsgstjWeek();
    List<TjTaskDto> xmList = taskTimeDao.gtTjTaskWeek();

    List<TaskTimeDto> taskTimeDtoList = new ArrayList<>();
    for (TaskTimeEntity taskTime : taskTimeLis) {
      TaskTimeDto taskTimeDto = new TaskTimeDto();
      BeanUtils.copyProperties(taskTime, taskTimeDto);
      if (taskTimeDto.getTtDate() != null) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        taskTimeDto.setTxDate(sdf.format(taskTimeDto.getTtDate()));
      }
      taskTimeDtoList.add(taskTimeDto);
    }

    // 从数据库读取收件人和收件组,抄送人和抄送组
    List<String> sjrs = new ArrayList<>();
    if (mailBox.getJsz() != null) {
      sjrs = dailyService.getEmail(mailBox.getJsz());
    }
    String jsr = mailBox.getJsr();
    if (!StringUtils.isEmpty(jsr)) {
      sjrs.addAll(Arrays.asList(jsr.split(",")));
    }

    List<String> csrs = new ArrayList<>();
    if (mailBox.getCsz() != null) {
      csrs = dailyService.getEmail(mailBox.getCsz());
    }
    String csr = mailBox.getCsr();
    if (!StringUtils.isEmpty(csr)) {
      csrs.addAll(Arrays.asList(csr.split(",")));
    }
    try {
      ReportFormUtil.projectForm(xmList, changeTasktimeWeek(taskTimeDtoList), adminUser, "每周发送工时统计", sjrs, csrs);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private List<TaskTimeDto> changeTasktimeWeek(List<TaskTimeDto> taskTimeDtoList) {
    Map<String, List<TaskTimeDto>> weekMap = Utils.groupMap(taskTimeDtoList, TaskTimeDto::getTxDate);
    List<TaskTimeDto> result = new ArrayList<>();
    for (Map.Entry<String, List<TaskTimeDto>> dto : weekMap.entrySet()) {
      result.addAll(changeTaskTime(dto.getValue()));
    }
    return result;

  }


  public void Universal(TjTaskVo taskVo, TjTaskVo task) {
    List<TjTaskVo> taskProject = taskTimeDao.getTaskProject(taskVo);
    for (int i = 0; i < taskProject.size(); i++) {
      if ("1".equals(taskProject.get(i).getTaskType())) {
        task.setDemandTime(taskProject.get(i).getSumTask());
      } else if ("2".equals(taskProject.get(i).getTaskType())) {
        task.setDevelTime(taskProject.get(i).getSumTask());
      } else if ("3".equals(taskProject.get(i).getTaskType())) {
        task.setTestTime(taskProject.get(i).getSumTask());
      } else if ("4".equals(taskProject.get(i).getTaskType())) {
        task.setImplementTime(taskProject.get(i).getSumTask());
      } else if ("5".equals(taskProject.get(i).getTaskType())) {
        task.setOtherTime(taskProject.get(i).getSumTask());
      }
    }
  }


}
