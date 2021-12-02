package com.sega.application.oa.service.system.impl;

import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.dao.system.IRoleModelDao;
import com.sega.application.oa.entity.system.ModelEntity;
import com.sega.application.oa.entity.system.RoleEntity;
import com.sega.application.oa.entity.system.RoleModelEntity;
import com.sega.application.oa.service.system.IModelService;
import com.sega.application.oa.service.system.IRoleModelService;
import com.sega.application.oa.service.system.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理持久化层
 * @author 邱小兵
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2017-8-1 21:16:48<br/>
 * 历史修订：<br/>
 */
 @Service("roleModelBizImpl")
public class RoleModelServiceImpl extends BaseServiceImpl implements IRoleModelService {

	
	@Autowired
	private IRoleModelDao roleModelDao;
	
	@Autowired
	private IRoleService roleBiz;
	
	@Autowired
	private IModelService modelBiz;
	
	
	@Override
	protected IBaseDao getDao() {
		return roleModelDao;
	}


	@Override
	public Integer saveByImportList(List<List<String>> list) {
		List<RoleEntity> roleList = roleBiz.queryAll();
		List<ModelEntity> modelList = modelBiz.queryAll();
		List<RoleModelEntity> roleModelList = new ArrayList(list.size());
		
		for (List<String> rowData : list) {
			Long roleId = null;
			String roleName = rowData.get(0);
			
			//查找roleName对应的roleId
			for (RoleEntity role : roleList) {
				if (roleName.equals(role.getRoleName())) {
					roleId = role.getRoleId();
				}
			}
			
			//查找roleName对应的模块编码，模块编码再转id
			for (int i = 1; i < rowData.size(); i++) {
				String modelCode = rowData.get(i);
				
				for (ModelEntity model : modelList) {
					if (modelCode.equals(model.getModelCode())) {
						Long modelId = model.getModelId();
						RoleModelEntity roleModel = new RoleModelEntity(roleId, modelId);
						
						roleModelList.add(roleModel);
					} 
				}
				
			}
		}
		
		if (roleModelList.size() != 0) {
			//删除全部角色权限数据
			roleModelDao.saveBatch(roleModelList);
		}
		return roleModelList.size();
	}
	
}