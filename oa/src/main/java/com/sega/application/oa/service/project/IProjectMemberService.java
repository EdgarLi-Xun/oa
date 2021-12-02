package com.sega.application.oa.service.project;

import com.sega.application.oa.service.system.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * 业务层
 * @author 孙乾
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2019-10-24 17:02:21<br/>
 * 历史修订：<br/>
 */
public interface IProjectMemberService extends IBaseService {

    List<Map<String,Object>> queryprojectList(String username);

    List<Map<String,Object>> queryplanprojectList(String username);

    void deleteByProjectId(Long projectId);
}
