package com.sega.application.oa.dao.system;

import com.sega.application.oa.entity.system.DepartmentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 持久层
 *
 * @author 邱小兵
 * @version 版本号：1.0.0<br/>
 * 创建日期：2017-8-2 16:00:18<br/>
 * 历史修订：<br/>
 */
@Mapper
public interface IDepartmentDao extends IBaseDao {

  /**
   * 获取所有公司
   *
   * @param department
   * @return
   */
  List queryCompany(DepartmentEntity department);

  /**
   * 根据ID查询实体信息
   *
   * @param id 实体ID
   * @return 返回实体
   */
  DepartmentEntity getByString(@Param(value = "ids") String ids);


  /**
   * 根据code获取条数
   *
   * @param departmentCode
   * @param departmentId
   * @return
   */
  int checkByDepartmentCode(@Param("departmentCode") String departmentCode, @Param("departmentId") Long departmentId);

    DepartmentEntity getEntityById(@Param("userDepartmentId") Long userDepartmentId);
}
