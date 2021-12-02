package com.sega.application.oa.service.system;

import com.sega.application.oa.entity.system.UserEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 业务
 *
 * @author 邱小兵
 * @version 版本号：1.0.0<br/>
 * 创建日期：2017-8-1 21:10:00<br/>
 * 历史修订：<br/>
 */
public interface IUserService extends IBaseService {

  /**
   * 根据账号获取用户实体
   *
   * @param account
   * @return
   */
  UserEntity getByAccount(String account);

  Boolean saveList(List<List<String>> list);

  /**
   * 根据模块编码查询分配了该菜单权限的所有用户
   *
   * @param modelCode
   * @return
   */
  List queryByModelCode(String modelCode);

  /**
   * 修改密码
   *
   * @param userEntity
   * @return
   */
  void changePassword(UserEntity userEntity);

  /**
   * 查询菜单编号
   *
   * @param request
   * @return
   */
  Map<String, Object> getMenuAuthCode(HttpServletRequest request);


  /**
   * 重置用户密码
   *
   * @param user
   */
  void resetUserPass(UserEntity user);

  /**
   * 判断当前用户名是否重复
   *
   * @param user
   * @return
   */
  Boolean checkByLoginName(UserEntity user);

  UserEntity getByUserId(Long userId);

  /**
   * 查询所有有效的用户
   *
   * @return
   */
  List queryAllUser();

    List<UserEntity> querySendUsers();
}
