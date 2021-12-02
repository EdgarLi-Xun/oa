package com.sega.application.oa.dao.system;


import com.sega.application.oa.entity.system.LogEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 持久层
 * @author 邱小兵
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2017-8-5 20:50:59<br/>
 * 历史修订：<br/>
 */
@Mapper
public interface ILogDao extends IBaseDao {
	
	/**
	 * 删除超过时间日志
	 * @param day
	 */
	void deleteByDay(Integer day);
	
	/**
	 * 查询日志分析数据
	 * @param log
	 * @return
	 */
	List analysis(LogEntity log);
}