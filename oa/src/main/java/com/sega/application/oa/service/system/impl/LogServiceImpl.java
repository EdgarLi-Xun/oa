package com.sega.application.oa.service.system.impl;

import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.dao.system.ILogDao;
import com.sega.application.oa.entity.system.LogEntity;
import com.sega.application.oa.service.system.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 管理持久化层
 * @author 邱小兵
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2017-8-5 20:50:59<br/>
 * 历史修订：<br/>
 */
 @Service("logBizImpl")
public class LogServiceImpl extends BaseServiceImpl implements ILogService {

	
	@Autowired
	private ILogDao logDao;
	
	
	@Override
	protected IBaseDao getDao() {
		return logDao;
	}


	@Override
	public void remain(Integer remainDay) {
		logDao.deleteByDay(remainDay);
	}


	@Override
	public List analysis(LogEntity log) {
		List<LogEntity> logList = logDao.analysis(log);
		
		List<HashMap<String, String>> logAnalysisList = new ArrayList<>();
		List<String> logModuleList = new ArrayList<>(10);
		
		
		for (LogEntity _log : logList) {
			String logModule = _log.getLogModule();//模块名作为值
			
			if (!logModuleList.contains(logModule)) {
				HashMap<String, String> logAnalysisMap = new HashMap<>();
				logAnalysisMap.put("logModule", logModule);
				logAnalysisList.add(logAnalysisMap);
				logModuleList.add(logModule);
			}
			
		}
		
		for (HashMap<String, String> logAnalysisMap : logAnalysisList) {
			String logModule = logAnalysisMap.get("logModule");
			
			for (LogEntity _log : logList) {
				String _logModule = _log.getLogModule();
				if (_logModule.equals(logModule)) {
					String logDepartmentId = _log.getLogDepartmentId().toString();//公司id作为键
					String logCount = _log.getLogCount();//统计数作为公司id的值
					logAnalysisMap.put(logDepartmentId, logCount);
				}
			}
			
		}
		
		return logAnalysisList;
	} 
	
}