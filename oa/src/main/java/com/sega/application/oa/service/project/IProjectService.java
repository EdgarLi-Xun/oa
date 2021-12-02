package com.sega.application.oa.service.project;

import com.sega.application.oa.entity.project.ProjectEntity;
import com.sega.application.oa.service.system.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * 业务层
 * @author 孙乾
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2019-10-21 20:11:43<br/>
 * 历史修订：<br/>
 */
public interface IProjectService extends IBaseService {

  List<Map<String,Object>>  queryuserByprojectId(ProjectEntity projectEntity);

  /**
   * 保存方法
   */
  void save(ProjectEntity projectEntity);

  /**
   * 更新方法
   */
  void update(ProjectEntity projectEntity);

  List<ProjectEntity> queryByPage(ProjectEntity project);
  ProjectEntity queryByProjectId(String projectId);
}
