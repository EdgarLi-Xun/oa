package com.sega.application.oa.service.project;

import com.sega.application.oa.entity.daily.dto.TjTaskDto;
import com.sega.application.oa.entity.daily.vo.TjTaskVo;
import com.sega.application.oa.entity.project.TaskTimeEntity;
import com.sega.application.oa.entity.project.vo.TaskTimeVo;
import com.sega.application.oa.service.system.IBaseService;

import java.util.List;

/**
 * 业务层
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2019-10-24 17:02:21<br/>
 * 历史修订：<br/>
 */
public interface ITaskTimeService extends IBaseService {

  /**
   * 查询下属工时
   *
   * @param taskTimeEntity
   * @return List
   */
  List queryChildrenTask(TaskTimeVo taskTimeEntity);

  List<TjTaskVo> getTjTaskTime(TjTaskVo tjTaskVo);

  // 分页查询
  List<TjTaskDto> queryByPage(TjTaskVo tjTaskVo);

  /**
   * 工时每天发送通知
   */
  void gsfsgstjDay();

  /**
   * 工时每周发送
   *
   */
  void gsfsgstjWeek();
}
