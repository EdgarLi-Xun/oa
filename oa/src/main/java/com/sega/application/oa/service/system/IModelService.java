package com.sega.application.oa.service.system;

import com.sega.application.oa.entity.system.ModelEntity;

import java.util.List;

/**
 * 业务
 *
 * @author 邱小兵
 * @version 版本号：1.0.0<br/>
 * 创建日期：2017-8-1 21:09:59<br/>
 * 历史修订：<br/>
 */
public interface IModelService extends IBaseService {

  /**
   * 查询未绑定在该角色下的菜单
   *
   * @param roleId
   * @return
   */
  List queryNotBind(Long roleId);

  /**
   * 根据id删除实体
   * 删除时将一并删除菜单权限中包含的数据
   *
   * @param id
   */
  @Override
  void deleteEntity(Long id);

  /**
   * 查询树形结构数据
   *
   * @param model
   * @return
   */
  List<ModelEntity> queryTree(Long roleId);

  /**
   * 查询当前编号是否被占用
   *
   * @param model
   * @return
   */
  boolean checkByModelCode(ModelEntity model);
}
