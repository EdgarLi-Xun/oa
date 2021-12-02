package com.sega.application.oa.dao.system;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 持久层
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2017-8-2 10:32:15<br/>
 * 历史修订：<br/>
 */
@Mapper
public interface IModelDao extends IBaseDao {

  /**
   * 查询未绑定在该角色下的菜单
   *
   * @param roleId
   * @return
   */
  List queryNotBind(Long roleId);

  /**
   * 检测当前code是否被占用
   *
   * @param modelCode
   * @param modelId
   * @return
   */
  int checkByModelCode(@Param("modelCode") String modelCode, @Param("modelId") Long modelId);
}
