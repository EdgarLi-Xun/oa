package com.sega.application.oa.dao;

import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.entity.daily.dto.TjTaskDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;


@Mapper
public interface IStatisticsDao extends IBaseDao {

  /**
   * 查询项目的统计时间
   * @param beforeDate
   * @param nowDate
   * @param tjTaskDto
   * @return
   */
  TjTaskDto queryxmSr(@Param("beforeDate") Date beforeDate,@Param("nowDate") Date nowDate, @Param("TjTaskDto") TjTaskDto tjTaskDto);

  /**
   * 查询员工的统计时间
   * @param beforeDate
   * @param nowDate
   * @param tjTaskDto
   * @return
   */
  List<TjTaskDto> queryygSr(@Param("beforeDate") Date beforeDate,@Param("nowDate") Date nowDate, @Param("TjTaskDto") TjTaskDto tjTaskDto);

  /**
   * 查询项目的id集合
   * @return
   */
  List<TjTaskDto> getObjectId();

  /**
   * 查询员工的id集合
   * @return
   */
  List<TjTaskDto> getUserId(@Param("num") Integer num);

  /**
   * 查询部门领导邮箱
   * @param depId
   * @return
   */
  String getSjr(int depId);
}
