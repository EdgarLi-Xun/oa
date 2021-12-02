package com.sega.application.oa.service.administration.impl;

import com.sega.application.oa.service.system.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sega.application.oa.dao.IFixedAssetsDao;
import com.sega.application.oa.service.administration.IFixedAssetsService;
import com.sega.application.oa.dao.system.IBaseDao;

import java.util.List;
import java.util.Map;

/**
 * 管理持久化层
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2019-10-24 17:02:21<br/>
 * 历史修订：<br/>
 */
 @Service
public class FixedAssetsServiceImpl extends BaseServiceImpl implements IFixedAssetsService {

	@Autowired
	private IFixedAssetsDao fixedAssetsDao;

    @Override
	protected IBaseDao getDao() {
		return fixedAssetsDao;
	}

    @Override
    public List<Map<String, Object>> queryStatistics() {
        return fixedAssetsDao.queryStatistics();
    }
}