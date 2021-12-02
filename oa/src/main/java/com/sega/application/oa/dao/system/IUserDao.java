package com.sega.application.oa.dao.system;

import com.sega.application.oa.entity.system.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 持久层
 *
 * @author 邱小兵
 * @version 版本号：1.0.0<br/>
 * 创建日期：2017-8-2 10:32:15<br/>
 * 历史修订：<br/>
 */
@Mapper
public interface IUserDao extends IBaseDao {

  UserEntity getByAccount(String account);

  UserEntity getByUserId(@Param("userId") Long userId);

  List queryByRoleIds(@Param("ids") String ids);

  UserEntity queryByUsernameAndPassword(UserEntity userEntity);

  int checkByLoginName(@Param("userLoginName") String userLoginName, @Param("userId") Long userId);

  /**
   * 查询所有的有效用户
   *
   * @return
   */
  List queryAllUser();

  List<UserEntity> querySendUsers();
}
