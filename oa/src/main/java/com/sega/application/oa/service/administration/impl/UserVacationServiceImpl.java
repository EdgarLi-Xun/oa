package com.sega.application.oa.service.administration.impl;

import com.sega.application.oa.service.system.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sega.application.oa.dao.IUserVacationDao;
import com.sega.application.oa.service.administration.IUserVacationService;
import com.sega.application.oa.dao.system.IBaseDao;

/**
 * 管理持久化层
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2019-10-24 17:02:21<br/>
 * 历史修订：<br/>
 */
 @Service
public class UserVacationServiceImpl extends BaseServiceImpl implements IUserVacationService {

	@Autowired
	private IUserVacationDao userVacationDao;

    @Override
	protected IBaseDao getDao() {
		return userVacationDao;
	}
	
}