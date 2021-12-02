package com.sega.application.oa.service.system.impl;

import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.dao.system.IRoleDataDao;
import com.sega.application.oa.entity.system.RoleDataEntity;
import com.sega.application.oa.service.system.IRoleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理持久化层
 * @author 邱小兵
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2017-8-9 17:34:14<br/>
 * 历史修订：<br/>
 */
 @Service("roleDataBizImpl")
public class RoleDataServiceImpl extends BaseServiceImpl implements IRoleDataService {

	
	@Autowired
	private IRoleDataDao roleDataDao;
	
	
	@Override
	protected IBaseDao getDao() {
		return roleDataDao;
	}

	/**
	 * 获取分配数据列表的ids
	 * 当List的size为0时，返回''
	 * @param roleDataList
	 * @return 
	 */
	@Override
	public String getIds(List<RoleDataEntity> roleDataList) {
		if (roleDataList.size() == 0) {
			return "''";
		}
		String ids = "";
		for (RoleDataEntity roleData : roleDataList) {
			ids += roleData.getRdValue() + ",";
		}
		ids = ids.substring(0, ids.length() - 1);
		return ids;
	}
	
	/**
	 * 获取分数数据列表的主键数组集合
	 * @param roleDataList
	 * @return
	 */
	public String[] getIdArray(List<RoleDataEntity> roleDataList) {
		if (roleDataList.size() == 0) {
			return null;
		}
		String[] idArray = new String[roleDataList.size()];
		for (int i = 0; i < roleDataList.size(); i++) {
			idArray[i] = roleDataList.get(i).getRdValue().toString();
		}
		return idArray;
	}

	@Override
	public void withdraw(RoleDataEntity roleData) {
		roleDataDao.withdraw(roleData);
	}
	
}