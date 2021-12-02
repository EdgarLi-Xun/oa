package com.sega.application.oa.service.daily.impl;

import com.sega.application.oa.dao.daily.IDailyfileDao;
import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.entity.daily.DailyfileEntity;
import com.sega.application.oa.service.daily.IDailyfileService;
import com.sega.application.oa.service.system.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理持久化层
 * @author 孙乾
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2020-3-4 17:09:47<br/>
 * 历史修订：<br/>
 */
 @Service
public class DailyfileServiceImpl extends BaseServiceImpl implements IDailyfileService {

	@Autowired
	private IDailyfileDao dailyfileDao;

    @Override
	protected IBaseDao getDao() {
		return dailyfileDao;
	}


    @Override
    public DailyfileEntity querydailyfile() {
        return dailyfileDao.querydailyfile();
    }

  @Override
  public void deleteById(Long id) {
    dailyfileDao.deleteById(id);
  }

    @Override
    public void deleteByDailyid(Long dailyId) {
      dailyfileDao.deleteByDailyid(dailyId);
    }

  @Override
  public List<DailyfileEntity> getByDailyid(Long dailyId) {
    return dailyfileDao.getByDailyid(dailyId);
  }
}
