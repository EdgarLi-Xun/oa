package com.sega.application.oa.service.system.impl;

import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.dao.system.IRoleDao;
import com.sega.application.oa.dao.system.IRoleModelDao;
import com.sega.application.oa.entity.system.RoleEntity;
import com.sega.application.oa.entity.system.RoleModelEntity;
import com.sega.application.oa.service.system.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 管理持久化层
 * @author 邱小兵
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2017-8-1 21:16:48<br/>
 * 历史修订：<br/>
 */
 @Service("roleBizImpl")
public class RoleServiceImpl extends BaseServiceImpl implements IRoleService {


	@Autowired
	private IRoleDao roleDao;

	@Autowired
	private IRoleModelDao roleModelDao;


	@Override
	protected IBaseDao getDao() {
		return roleDao;
	}


	@Override
	public void copyRole(RoleEntity role, Long copyRoleId) {
		roleDao.saveEntity(role);

		//根据copyRoleId获取拷贝对象的角色菜单权限
		List<RoleModelEntity> roleModelList = roleModelDao.query(new RoleModelEntity(copyRoleId));
		for (RoleModelEntity roleModel : roleModelList) {
			roleModel.setRmRoleId(role.getRoleId());
		}

		roleModelDao.saveBatch(roleModelList);
	}

	/*
	 * 权限分配
	 */
	public void permissionAssign(List<Long> modelIds,Long roleId){
		RoleModelEntity roleModelEntity = new RoleModelEntity();
		roleModelEntity.setRmRoleId(roleId);
		roleModelDao.deleteEntity(roleModelEntity);
		for(Long modelId : modelIds){
			RoleModelEntity _roleModel = new RoleModelEntity();
			_roleModel.setRmRoleId(roleId);
			_roleModel.setRmModelId(modelId);
			roleModelDao.saveEntity(_roleModel);
		}

	}

    @Override
    public boolean checkByRoleName(RoleEntity role) {
        return roleDao.checkByRoleName(role.getRoleName(),role.getRoleId())==0;
    }


    /**
	 * 根据ids删除角色，删除时将一并删除角色所分配的菜单权限
	 * @param ids
	 */
	@Override
	public void deleteEntity(String[] idArray) {
		roleDao.deleteByIds(idArray);
		roleModelDao.deleteByIds(idArray);
	}

}
