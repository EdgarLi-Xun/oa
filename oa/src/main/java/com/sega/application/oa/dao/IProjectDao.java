package com.sega.application.oa.dao;

import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.entity.project.ProjectEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 持久层
 * @author 孙乾
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2019-10-21 19:56:16<br/>
 * 历史修订：<br/>
 */
@Mapper
public interface IProjectDao extends IBaseDao {
    List<Map<String,Object>> queryuserByprojectId(@Param("projectId") Long projectId);
    List<ProjectEntity> queryByPage(ProjectEntity project);
    ProjectEntity queryByProjectId(@Param("projectId") String projectId);
}
