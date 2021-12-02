package com.sega.application.oa.service.system;

import com.sega.application.oa.entity.system.DepartmentEntity;

import java.util.List;

/**
 * 业务
 *
 * @author 邱小兵
 * @version 版本号：1.0.0<br/>
 * 创建日期：2017-8-1 21:09:59<br/>
 * 历史修订：<br/>
 */
public interface IDepartmentService extends IBaseService {
  public Boolean saveList(List<List<String>> list);

  /**
   * 获取所有公司
   *
   * @param department
   * @return
   */
  List queryCompany(DepartmentEntity department);

  /**
   * 查询方法重写
   *
   * @param department
   * @return
   */
  List queryByCompany(DepartmentEntity department);

  /**
   * 查询部门树状结构数据
   *
   * @return
   */
  List<DepartmentEntity> queryTreeData();

  /**
   * 通过部门编号查询是否重复
   *
   * @param department
   * @return
   */
  boolean checkByDepartmentCode(DepartmentEntity department);

    DepartmentEntity getEntityById(Long userDepartmentId);
}
