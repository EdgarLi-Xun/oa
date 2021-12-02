package com.sega.application.oa.dao;

import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.entity.daily.dto.TjTaskDto;
import com.sega.application.oa.entity.daily.vo.TjTaskVo;
import com.sega.application.oa.entity.project.TaskTimeEntity;
import com.sega.application.oa.entity.project.vo.TaskTimeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

/**
 * 持久层
 * @author 孙乾
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2019-10-24 17:02:21<br/>
 * 历史修订：<br/>
 */
@Mapper
public interface ITaskTimeDao extends IBaseDao {
  /**
   * 根据部门id查询工时
   * @return
   */
  List queryChildrenTask(TaskTimeVo taskTimeEntity);

  /**
   * 统计工时
   * @param tjTaskVo
   * @return
   */
  List<TjTaskVo> getTaskProject(@Param("vo") TjTaskVo tjTaskVo);

  List<TjTaskDto> queryByPage(@Param("vo") TjTaskVo tjTaskVo);


  TjTaskDto queryxmSr(@Param("beforeDate") Date beforeDate,@Param("nowDate") Date nowDate, @Param("TjTaskDto") TjTaskDto tjTaskDto);

  List<TjTaskDto> queryygSr(@Param("beforeDate") Date beforeDate,@Param("nowDate") Date nowDate, @Param("TjTaskDto") TjTaskDto tjTaskDto);

  List<TjTaskDto> getObjectId();

  List<TjTaskDto> getUserId();

    List<TaskTimeEntity> gsfsgstjDay();

  List<TjTaskDto> gtTjTaskDay();
  List<TjTaskDto> gtTjTaskWeek();

  List<TaskTimeEntity> gsfsgstjWeek();
}
