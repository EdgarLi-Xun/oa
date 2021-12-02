package com.sega.application.oa.service.system;

import com.sega.application.oa.entity.system.RoleDataEntity;

import java.util.List;

/**
 * 业务
 * @author 邱小兵
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2017-8-9 17:34:14<br/>
 * 历史修订：<br/>
 */
public interface IRoleDataService extends IBaseService {

	/**
	 * 获取分配数据列表的ids
	 * @param roleDataList
	 * @return
	 */
	String getIds(List<RoleDataEntity> roleDataList);
	
	/**
	 * 获取分数数据列表的主键数组集合
	 * @param roleDataList
	 * @return
	 */
	String[] getIdArray(List<RoleDataEntity> roleDataList);
	
	/**
	 * 撤回申请
	 * @param roleData
	 */
	void withdraw(RoleDataEntity roleData);
}