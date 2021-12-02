package com.sega.application.oa.dao.system;


import com.sega.application.oa.entity.system.RoleDataEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 持久层
 * @author 邱小兵
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2017-8-9 17:34:14<br/>
 * 历史修订：<br/>
 */
@Mapper
public interface IRoleDataDao extends IBaseDao {
	
	/**
	 * 撤回申请
	 * @param roleData
	 */
	void withdraw(RoleDataEntity roleData);
}