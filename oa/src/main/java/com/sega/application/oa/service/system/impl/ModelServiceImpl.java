package com.sega.application.oa.service.system.impl;

import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.dao.system.IModelDao;
import com.sega.application.oa.dao.system.IRoleModelDao;
import com.sega.application.oa.entity.system.ModelEntity;
import com.sega.application.oa.entity.system.RoleModelEntity;
import com.sega.application.oa.service.system.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理持久化层
 *
 * @author 邱小兵
 * @version 版本号：1.0.0<br/>
 * 创建日期：2017-8-1 21:16:47<br/>
 * 历史修订：<br/>
 */
@Service("modelBizImpl")
public class ModelServiceImpl extends BaseServiceImpl implements IModelService {


  @Autowired
  private IModelDao modelDao;

  @Autowired
  private IRoleModelDao roleModelDao;


  @Override
  protected IBaseDao getDao() {
    return modelDao;
  }

  /**
   * 查询未绑定在该角色下的菜单
   *
   * @param roleId
   * @return
   */
  @Override
  public List queryNotBind(Long roleId) {
    return modelDao.queryNotBind(roleId);
  }

  /**
   * 根据id删除实体
   * 删除时将一并删除菜单权限中包含的数据
   *
   * @param id
   */
  @Override
  public void deleteEntity(Long id) {
    modelDao.deleteById(id);

    RoleModelEntity roleModel = new RoleModelEntity();
    roleModel.setRmModelId(id);
    roleModelDao.deleteEntity(roleModel);
  }

  /**
   * 查询树形结构数据
   *
   * @param roleId
   * @return
   */
  public List<ModelEntity> queryTree(Long roleId) {
    List<ModelEntity> modelEntities = modelDao.queryAll();
    if (roleId != null) {
      RoleModelEntity roleModelEntity = new RoleModelEntity();
      roleModelEntity.setRmRoleId(roleId);
      List<RoleModelEntity> rmList = roleModelDao.query(roleModelEntity);
      for (RoleModelEntity _roleModel : rmList) {
        for (ModelEntity modelEntity : modelEntities) {
          if (_roleModel.getRmModelId().equals(modelEntity.getModelId())) {
            modelEntity.set_isChecked(true);
          }
        }
      }
    }
    return this.buildByRecursive(modelEntities);
  }

  @Override
  public boolean checkByModelCode(ModelEntity model) {
    return modelDao.checkByModelCode(model.getModelCode(), model.getModelId()) == 0;
  }

  /**
   * 使用递归方法建树
   *
   * @param treeNodes
   * @return
   */
  public static List<ModelEntity> buildByRecursive(List<ModelEntity> treeNodes) {
    List<ModelEntity> trees = new ArrayList<ModelEntity>();
    for (ModelEntity treeNode : treeNodes) {
      if (treeNode.getModelParentId() == null) {
        trees.add(findChildren(treeNode, treeNodes));
      }
    }
    return trees;
  }

  /**
   * 递归查找子节点
   *
   * @param treeNodes
   * @return
   */
  public static ModelEntity findChildren(ModelEntity treeNode, List<ModelEntity> treeNodes) {
    for (ModelEntity it : treeNodes) {
      if (treeNode.getModelId().equals(it.getModelParentId())) {
        if (treeNode.getChildren() == null) {
          treeNode.setChildren(new ArrayList<ModelEntity>());
        }
        treeNode.getChildren().add(findChildren(it, treeNodes));
      }
    }
    return treeNode;
  }
}
