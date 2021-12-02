package com.sega.application.oa.service.daily.impl;

import com.sega.application.oa.entity.daily.DailySenderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sega.application.oa.dao.daily.IDailySenderDao;
import com.sega.application.oa.service.daily.IDailySenderService;
import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.service.system.impl.BaseServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * 管理持久化层
 * @author 孙乾
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:25:28<br/>
 * 历史修订：<br/>
 */
 @Service
public class DailySenderServiceImpl extends BaseServiceImpl implements IDailySenderService {

	@Autowired
	private IDailySenderDao dailySenderDao;

    @Override
	protected IBaseDao getDao() {
		return dailySenderDao;
	}

	@Override
	public List<DailySenderEntity> querysendByPage(DailySenderEntity dailySenderEntity) {
		return dailySenderDao.querysendByPage(dailySenderEntity);
	}

	@Override
	public List<Map<String, Object>> queryjsrEmail(String username) {
		return dailySenderDao.queryjsrEmail(username);
	}

	@Override
	public List<Map<String, Object>> querycsrEmail(String username) {
		return dailySenderDao.querycsrEmail(username);
	}

  @Override
  public Map<String, Object> queryJsrAndCsrEmail(Long userId) {
    return dailySenderDao.queryJsrAndCsrEmail(userId);
  }
}
