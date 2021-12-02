package com.sega.application.oa.service.system;

import com.sega.application.oa.entity.system.RoleEntity;

import java.util.List;

/**
 * 业务
 *
 * @author 邱小兵
 * @version 版本号：1.0.0<br/>
 * 创建日期：2017-8-1 21:09:59<br/>
 * 历史修订：<br/>
 */
public interface IRoleService extends IBaseService {

  /**
   * 角色拷贝
   * 拷贝内容：角色权限
   *
   * @param role
   * @param copyRoleId
   */
  void copyRole(RoleEntity role, Long copyRoleId);

  /**
   * 根据ids删除角色，删除时将一并删除角色所分配的菜单权限
   *
   * @param ids
   */
  @Override
  void deleteEntity(String[] ids);

  /**
   * 角色权限分配
   *
   * @param modelIds
   * @param roleId
   */
  public void permissionAssign(List<Long> modelIds, Long roleId);

  /**
   * 根据角色名称查询当前是否存在
   *
   * @param role
   * @return
   */
  boolean checkByRoleName(RoleEntity role);
}
