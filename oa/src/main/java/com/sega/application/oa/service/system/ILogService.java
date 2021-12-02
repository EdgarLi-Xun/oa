package com.sega.application.oa.service.system;

import com.sega.application.oa.entity.system.LogEntity;

import java.util.List;

/**
 * 业务
 * @author 邱小兵
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2017-8-5 20:50:59<br/>
 * 历史修订：<br/>
 */
public interface ILogService extends IBaseService {
	
	/**
	 * 保留限定天数日志
	 * @param remainDay
	 */
	void remain(Integer remainDay);
	
	/**
	 * 查询日志分析数据
	 * @param log
	 * @return
	 */
	List analysis(LogEntity log);
}