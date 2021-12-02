package com.sega.application.oa.service.project.impl;

import com.sega.application.oa.service.system.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sega.application.oa.dao.IProjectMemberDao;
import com.sega.application.oa.service.project.IProjectMemberService;
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
public class ProjectMemberServiceImpl extends BaseServiceImpl implements IProjectMemberService {

	@Autowired
	private IProjectMemberDao projectMemberDao;

    @Override
	protected IBaseDao getDao() {
		return projectMemberDao;
	}


	@Override
	public List<Map<String, Object>> queryprojectList(String username) {
		return projectMemberDao.queryprojectList(username);
	}

	@Override
	public List<Map<String, Object>> queryplanprojectList(String username) {
		return projectMemberDao.queryplanprojectList(username);
	}

  @Override
  public void deleteByProjectId(Long projectId) {
    projectMemberDao.deleteByProjectId(projectId);
  }
}
