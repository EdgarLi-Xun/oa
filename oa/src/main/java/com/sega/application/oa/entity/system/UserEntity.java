package com.sega.application.oa.entity.system;

import com.sega.application.oa.annotation.Id;
import com.sega.application.oa.annotation.Table;
import lombok.Data;

import java.util.Date;


/**
 * 实体
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2019-10-22 17:27:02<br/>
 * 历史修订：<br/>
 */
@Data
@Table(value = "user")
public class UserEntity extends BaseEntity {


  /**
   * 部门
   */
  private DepartmentEntity department;

  /**
   * 角色
   */
  private RoleEntity role;

  /**
   * 员工Id
   */
  @Id
  private Long userId;

  /**
   *
   */
  private String userName;

  /**
   * 密码
   */
  private String userPassword;
  /**
   * 确认密码
   */
  private String confirmPassword;
  /**
   * 原密码
   */
  private String originPassword;

  /**
   * 所属部门
   */
  private Long userDepartmentId;

  /**
   * 用户角色Id
   */
  private Long userRoleId;

  /**
   * 0:在职
   * 1：离职
   */
  private Integer userState;

  /**
   * 用户邮箱
   */
  private String userEmail;

  /**
   * 用户邮箱密码
   */
  private String emailPassword;


  /**
   *
   */
  private String userTelephone;

  /**
   * 登录名
   */
  private String userLoginName;

  /**
   * 0:男，1:女
   */
  private Integer userGender;

  /**
   * 无需发日报
   * 0:需要发送，1:无需发日报
   */
  private Integer daily;

  /**
   * 年龄
   */
  private Integer userAge;

  /**
   * 省份证
   */
  private String userIdCard;

  /**
   * 地址
   */
  private String userAddress;

  /**
   *
   */
  private Long userCreateBy;

  /**
   *
   */
  private Date userCreateDate;

  /**
   *
   */
  private Long userUpdateBy;

  /**
   * 部门名称
   */
  private String departmentName;

  /**
   * 角色名称
   */
  private String roleName;

  /**
   *
   */
  private Date userUpdateDate;

  /**
   * 是否删除：0:未删除，1：已删除
   */
  private Integer userDel;


  /**
   * 1管理、2开发、3测试、4实施
   */
  private Integer userTaskTime;


}
