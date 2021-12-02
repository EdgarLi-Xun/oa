package com.sega.application.oa.service.project.impl;

import com.sega.application.oa.entity.project.ProjectMemberEntity;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.project.IProjectMemberService;
import com.sega.application.oa.service.system.IUserService;
import com.sega.application.oa.service.system.impl.BaseServiceImpl;
import com.sega.application.oa.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.sega.application.oa.dao.IProjectDao;
import com.sega.application.oa.entity.project.ProjectEntity;
import com.sega.application.oa.service.project.IProjectService;
import com.sega.application.oa.dao.system.IBaseDao;

/**
 * 管理持久化层
 * @author 孙乾
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2019-10-21 20:16:23<br/>
 * 历史修订：<br/>
 */
 @Service
public class ProjectServiceImpl extends BaseServiceImpl implements IProjectService {

	@Autowired
	private IProjectDao projectDao;
	@Autowired
  private IUserService userService;
	@Autowired
  private IProjectMemberService projectMemberService;

  @Override
  public ProjectEntity queryByProjectId(String projectId) {
    return projectDao.queryByProjectId(projectId);
  }
  @Override
	protected IBaseDao getDao() {
		return projectDao;
	}

	@Override
	public List<Map<String, Object>> queryuserByprojectId(ProjectEntity projectEntity) {
		return projectDao.queryuserByprojectId(projectEntity.getProjectId());
	}

  @Override
  public void save(ProjectEntity projectEntity) {
    projectDao.saveEntity(projectEntity);
    //保存成员
    saveProjectMember(projectEntity);
  }

  @Override
  public void update(ProjectEntity projectEntity) {
    projectDao.updateEntity(projectEntity);
    //删除成员
    projectMemberService.deleteByProjectId(projectEntity.getProjectId());
    //保存成员
    saveProjectMember(projectEntity);

  }

  @Override
  public List<ProjectEntity> queryByPage(ProjectEntity project) {
    List<ProjectEntity> projectEntityList = projectDao.queryByPage(project);
    if(projectEntityList != null && projectEntityList.size() > 0){
      for(ProjectEntity projectEntity : projectEntityList){
        if(StringUtils.isNotBlank(projectEntity.getProjectMemberStr())){
          projectEntity.setProjectMembers(StringUtil.splitStringToLongArray(projectEntity.getProjectMemberStr()));
        }
      }
    }
    return projectEntityList;
  }

  private void saveProjectMember(ProjectEntity projectEntity) {
    if (ArrayUtils.isNotEmpty(projectEntity.getProjectMembers())) {
      List<ProjectMemberEntity> projectMemberList = new ArrayList<>();
      for (Long userId : projectEntity.getProjectMembers()) {
        ProjectMemberEntity projectMember = new ProjectMemberEntity();
        UserEntity user = (UserEntity) userService.getEntity(userId);
        projectMember.setPmUserId(user.getUserId());
        projectMember.setPmUserName(user.getUserName());
        projectMember.setPmProjectId(projectEntity.getProjectId());
        projectMember.setPmCreateDate(new Date());
        projectMember.setPmCreateBy(projectEntity.getProjectCreateBy());
        projectMember.setPmDel(0);
        projectMemberList.add(projectMember);
      }
      projectMemberService.saveEntity(projectMemberList);
    }
  }
}
