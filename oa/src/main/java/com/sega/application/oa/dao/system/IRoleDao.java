package com.sega.application.oa.dao.system;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 持久层
 *
 * @author 邱小兵
 * @version 版本号：1.0.0<br/>
 * 创建日期：2017-8-2 10:32:15<br/>
 * 历史修订：<br/>
 */
@Mapper
public interface IRoleDao extends IBaseDao {
  /**
   * 根据角色名查询是否存在
   *
   * @param roleName
   * @param roleId
   * @return
   */
  int checkByRoleName(@Param("roleName") String roleName, @Param("roleId") Long roleId);
}
