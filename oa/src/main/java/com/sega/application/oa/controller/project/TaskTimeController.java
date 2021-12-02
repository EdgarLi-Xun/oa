package com.sega.application.oa.controller.project;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.controller.system.BaseController;
import com.sega.application.oa.entity.daily.dto.TjTaskDto;
import com.sega.application.oa.entity.daily.vo.TjTaskVo;
import com.sega.application.oa.entity.project.ProjectEntity;
import com.sega.application.oa.entity.project.vo.TaskTimeVo;
import com.sega.application.oa.entity.system.DepartmentEntity;
import com.sega.application.oa.service.project.IProjectService;
import com.sega.application.oa.service.project.ITaskTimeService;
import com.sega.application.oa.entity.project.TaskTimeEntity;
import com.sega.application.oa.service.system.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import com.sega.application.oa.entity.system.OutData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 管理控制层
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2019-10-24 17:02:21<br/>
 * 历史修订：<br/>
 */
@RestController
@CrossOrigin
@RequestMapping("/apis/taskTime")
public class TaskTimeController extends BaseController {

  private static final Logger log = LoggerFactory.getLogger(TaskTimeController.class);
  /**
   * 注入业务层
   */
  @Autowired
  private ITaskTimeService taskTimeService;

  @Autowired
  private IDepartmentService departmentService;

  @Autowired
  private IProjectService projectService;


  @PostMapping("/getTjTask")
  @ResponseBody
  public void getTjTask(@RequestBody TjTaskVo taskVo, HttpServletResponse response, HttpServletRequest request) {
    try {
      if (taskVo.getPageNum() == null) {
        taskVo.setPageNum(0);
      }
      if (taskVo.getPageSize() == null) {
        taskVo.setPageSize(20);
      }
      if (taskVo.getStartDate() != null) {
        try {
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
          String start = dateFormat.format(taskVo.getStartDate());
          java.sql.Date startDate = new java.sql.Date(dateFormat.parse(start).getTime());
          taskVo.setStartDate(startDate);
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
      if (taskVo.getEndDate() != null) {
        try {
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
          String end = dateFormat.format(taskVo.getEndDate());
          java.sql.Date endDate = new java.sql.Date(dateFormat.parse(end).getTime());
          taskVo.setEndDate(endDate);
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
      PageHelper.startPage(taskVo.getPageNum(), taskVo.getPageSize());
      List<TjTaskDto> list = taskTimeService.queryByPage(taskVo);
      this.outJson(response, new OutData(true, "查询成功", new PageInfo<TjTaskDto>(list)), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(true, "查询失败"), "yyyy-MM-dd");
    }
  }


  /**
   * 新增或更新数据
   *
   * @param taskTime
   * @return
   */
  @PostMapping("/saveOrUpdate")
  @ResponseBody
  public void saveOrUpdate(@RequestBody TaskTimeEntity taskTime, HttpServletResponse response, HttpServletRequest request) {
    try {
      if (taskTime.getTtId() == null) {
        ProjectEntity projectEntity = projectService.queryByProjectId(String.valueOf(taskTime.getTtProjectId()));
        if (projectEntity == null) {
          this.outJson(response, new OutData(false, "项目未找到"), "yyyy-MM-dd");
        }
        if ("".equals(taskTime.getTtProjectName())) {
          taskTime.setTtProjectName(projectEntity.getProjectName());
        }
        taskTime.setTtCreateDate(new Date());
        taskTime.setTtCreateBy(this.getUserBySession(request).getUserId());
        taskTime.setTtUserId(this.getUserBySession(request).getUserId());
        taskTime.setTtUserName(this.getUserBySession(request).getUserName());
        taskTime.setTtState("1");
        taskTimeService.saveEntity(taskTime);
        this.outJson(response, new OutData(true, "新增成功", taskTime), "yyyy-MM-dd");
      } else {
        taskTime.setTtUpdateBy(this.getUserBySession(request).getUserId());
        taskTime.setTtUpdateDate(new Date());
        taskTimeService.updateEntity(taskTime);
        this.outJson(response, new OutData(true, "更新成功", taskTime), "yyyy-MM-dd");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      if (taskTime.getTtId() == null) {
        this.outJson(response, new OutData(false, "新增失败"), "yyyy-MM-dd");
      } else {
        this.outJson(response, new OutData(false, "更新失败"), "yyyy-MM-dd");
      }
    }
  }

  /**
   * 逻辑删除
   *
   * @param tid
   * @return
   */
  @GetMapping("/delete/{tid}")
  @ResponseBody
  public void delete(@PathVariable Long tid, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
    try {
      TaskTimeEntity taskTime = new TaskTimeEntity();
      taskTime.setTtId(tid);
      taskTime.setTtDel(1);
      taskTime.setTtUpdateDate(new Date());
      taskTimeService.updateEntity(taskTime);
      this.outJson(response, new OutData(false, "删除成功", taskTime), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      this.outJson(response, new OutData(false, "删除失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 逻辑删除
   *
   * @param tids
   * @return
   */
  @PostMapping("/deleteByIds")
  @ResponseBody
  public void deleteByIds(@RequestBody String tids, HttpServletResponse response, HttpServletRequest request) {
    try {
      List<String> ids = JSONArray.parseArray(tids, String.class);

      taskTimeService.deleteEntity(ids.toArray(new String[ids.size()]));

      this.outJson(response, new OutData(false, "删除成功"), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      this.outJson(response, new OutData(false, "删除失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 按条件分页查询数据
   *
   * @param taskTime
   * @return
   */
  @PostMapping("/queryByPage")
  @ResponseBody
  public void queryByPage(@RequestBody TaskTimeEntity taskTime, HttpServletResponse response, HttpServletRequest request) {
    try {

      if (taskTime.getPageNum() == null) {
        taskTime.setPageNum(0);
      }
      if (taskTime.getPageSize() == null) {
        taskTime.setPageSize(20);
      }
      PageHelper.startPage(taskTime.getPageNum(), taskTime.getPageSize());

      // 测试先不修改userId的值
      if (this.getUserBySession(request).getUserId() != 999) {
        taskTime.setTtUserId(this.getUserBySession(request).getUserId());
      }

      List<TaskTimeEntity> list = taskTimeService.query(taskTime);
      this.outJson(response, new OutData(true, "查询成功", new PageInfo<TaskTimeEntity>(list)), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(true, "查询失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 按条件查询数据
   *
   * @param taskTime
   * @return
   */
  @PostMapping("/queryAll")
  @ResponseBody
  public void queryAll(@RequestBody TaskTimeEntity taskTime, HttpServletResponse response, HttpServletRequest request) {
    try {
      taskTime.setTtUserId(this.getUserBySession(request).getUserId());

      List<TaskTimeEntity> list = taskTimeService.query(taskTime);
      this.outJson(response, new OutData(true, "查询成功", list), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(false, "查询失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 根据ID获取数据
   *
   * @param id
   * @return
   */
  @GetMapping("/get/{id}")
  @ResponseBody
  public void get(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) {
    try {
      this.outJson(response, new OutData(true, "查询成功", taskTimeService.getEntity(id)), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(false, "查询失败"), "yyyy-MM-dd");
    }
  }


  /**
   * 按条件分页查询数据
   *
   * @param taskTime
   * @return
   */
  @PostMapping("/queryChildrenTask")
  @ResponseBody
  public void queryChildrenTask(@RequestBody TaskTimeVo taskTime, HttpServletResponse response, HttpServletRequest request) {
    try {

      if (taskTime.getPageNum() == null) {
        taskTime.setPageNum(0);
      }
      if (taskTime.getPageSize() == null) {
        taskTime.setPageSize(20);
      }
      PageHelper.startPage(taskTime.getPageNum(), taskTime.getPageSize());
//      DepartmentEntity departmentEntity = departmentService.getEntityById(this.getUserBySession(request).getUserDepartmentId());
//      taskTime.setCode(departmentEntity.getDepartmentCode());
//      List<TaskTimeEntity> list =taskTimeService.query(taskTime);
      Long userId = this.getUserBySession(request).getUserId();
      if (userId != 999) {
        taskTime.setTtUserId(userId);
      }

      List<TaskTimeEntity> list = taskTimeService.queryChildrenTask(taskTime);
      this.outJson(response, new OutData(true, "查询成功", new PageInfo<TaskTimeEntity>(list)), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(true, "查询失败"), "yyyy-MM-dd");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
