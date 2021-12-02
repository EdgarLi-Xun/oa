package com.sega.application.oa.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sega.application.oa.dao.system.IFileDao;
import com.sega.application.oa.service.system.IFileService;
import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.service.system.impl.BaseServiceImpl;

/**
 * 管理持久化层
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:25:28<br/>
 * 历史修订：<br/>
 */
 @Service
public class FileServiceImpl extends BaseServiceImpl implements IFileService {

	@Autowired
	private IFileDao fileDao;

    @Override
	protected IBaseDao getDao() {
		return fileDao;
	}
	
}