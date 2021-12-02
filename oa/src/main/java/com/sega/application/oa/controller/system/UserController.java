package com.sega.application.oa.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.entity.system.OutData;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.system.IUserService;
import com.sega.application.oa.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 管理控制层
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2019-10-22 17:27:02<br/>
 * 历史修订：<br/>
 */
@Controller
@CrossOrigin
@RequestMapping("/apis/user")
public class UserController extends BaseController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);
  /**
   * 注入业务层
   */
  @Autowired
  private IUserService userService;

  @Value("${sega.password}")
  private String password;

  /**
   * 新增或更新数据
   *
   * @param user
   * @return
   */
  @PostMapping("/saveOrUpdate")
  @ResponseBody
  public void saveOrUpdate(@RequestBody UserEntity user, HttpServletResponse response, HttpServletRequest request) {
    try {
      if (!userService.checkByLoginName(user)) {
        this.outJson(response, new OutData(false, "用户名重复", user), "yyyy-MM-dd");
      } else if (user.getUserId() == null) {

        user.setUserCreateDate(new Date());
        user.setUserCreateBy(this.getUserBySession(request).getUserId());
        user.setUserPassword(StringUtil.MD5(password));
        userService.saveEntity(user);
        this.outJson(response, new OutData(true, "新增成功", user), "yyyy-MM-dd");
      } else {
        userService.updateEntity(user);
        user.setUserUpdateBy(this.getUserBySession(request).getUserId());
        user.setUserUpdateDate(new Date());
        this.outJson(response, new OutData(true, "更新成功", user), "yyyy-MM-dd");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      if (user.getUserId() == null) {
        this.outJson(response, new OutData(false, "新增失败"), "yyyy-MM-dd");
      } else {
        this.outJson(response, new OutData(false, "更新失败"), "yyyy-MM-dd");
      }
    }
  }

  /**
   * 修改密码
   *
   * @param user
   * @return
   */
  @PostMapping("/changePassword")
  @ResponseBody
  public void changePassword(@RequestBody UserEntity user, HttpServletResponse response, HttpServletRequest request) {
    try {
      if (StringUtils.isBlank(user.getUserPassword())) {
        throw new RuntimeException("新密码为空，密码修改失败！");
      }

      if (StringUtils.isBlank(user.getOriginPassword())) {
        throw new RuntimeException("原密码为空，密码修改失败！");
      }

      if (!user.getUserPassword().equals(user.getConfirmPassword())) {
        throw new RuntimeException("密码与确认密码不一致，密码修改失败！");
      }

      userService.changePassword(user);

      this.outJson(response, new OutData(true, "密码修改成功"), "yyyy-MM-dd");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(false, e.getMessage()), "yyyy-MM-dd");
    }
  }
  /**
   * 邮箱密码
   *
   * @param user
   * @return
   */
  @PostMapping("/changeEmailPassword")
  @ResponseBody
  public void changeEmailPassword(@RequestBody UserEntity user, HttpServletResponse response, HttpServletRequest request) {
    try {
      if(StringUtils.isBlank(user.getEmailPassword())){
        throw new RuntimeException("新密码为空，密码修改失败！");
      }
      userService.updateEntity(user);
      this.outJson(response, new OutData(true, "密码修改成功"), "yyyy-MM-dd");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(false, e.getMessage()), "yyyy-MM-dd");
    }
  }
  /**
   * 逻辑删除
   *
   * @param tid
   * @return
   */
  @DeleteMapping("/delete/{tid}")
  @ResponseBody
  public void delete(@PathVariable Long tid, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
    try {
      UserEntity user = new UserEntity();
      user.setUserId(tid);
      user.setUserDel(1);
      user.setUserUpdateDate(new Date());
      userService.updateEntity(user);
      this.outJson(response, new OutData(false, "删除成功", user), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      this.outJson(response, new OutData(false, "删除失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 按条件分页查询数据
   *
   * @param user
   * @return
   */
  @PostMapping("/queryByPage")
  @ResponseBody
  public void queryByPage(@RequestBody UserEntity user, HttpServletResponse response, HttpServletRequest request) {
    try {
      if (user.getPageNum() == null) {
        user.setPageNum(0);
      }
      if (user.getPageSize() == null) {
        user.setPageSize(20);
      }
      PageHelper.startPage(user.getPageNum(), user.getPageSize());
      List<UserEntity> list = userService.query(user);

      this.outJson(response, new OutData(true, "查询成功", new PageInfo<UserEntity>(list)), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(true, "查询失败"), "yyyy-MM-dd");
    }
  }

  @PostMapping("/resetUserPass")
  @ResponseBody
  public void resetUserPass(@RequestBody UserEntity user, HttpServletResponse response, HttpServletRequest request) {
    try {
      userService.resetUserPass(user);
      this.outJson(response, new OutData(true, "重置成功"), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(true, "重置失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 逻辑删除
   *
   * @param tids
   * @return
   */
  @PostMapping("/deleteByIds")
  @ResponseBody
  public void delete(@RequestBody String tids, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
    try {
      List<Long> ids = JSONArray.parseArray(tids, Long.class);
      for (Long tid : ids) {
        UserEntity user = new UserEntity();
        user.setUserId(tid);
        user.setUserDel(1);
        user.setUserUpdateDate(new Date());
        userService.updateEntity(user);
      }
      this.outJson(response, new OutData(false, "删除成功"), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      this.outJson(response, new OutData(false, "删除失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 按条件查询数据
   *
   * @param user
   * @return
   */
  @PostMapping("/queryAll")
  @ResponseBody
  public void queryAll(UserEntity user, HttpServletResponse response, HttpServletRequest request) {
    try {
      List<UserEntity> list = userService.query(user);
      this.outJson(response, new OutData(true, "查询成功", list), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(false, "查询失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 根据ID获取数据
   *
   * @param id
   * @return
   */
  @GetMapping("/get/{id}")
  @ResponseBody
  public void get(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) {
    try {
      this.outJson(response, new OutData(true, "查询成功", userService.getEntity(id)), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(false, "查询失败"), "yyyy-MM-dd");
    }
  }


  /**
   * 查询全部
   * <p>
   * 实体
   *
   * @param response
   * @param request
   */
  @RequestMapping("/getUserInfo")
  @ResponseBody
  public void getUserInfo(HttpServletResponse response, HttpServletRequest request) {
    this.outJson(response, getUserBySession(request), "yyyy-MM-dd");
  }

  /**
   * 查看当前用户的所有权限信息
   *
   * @param response
   * @param request
   */
  @RequestMapping("/getMenuAuthCode")
  @ResponseBody
  public void getMenuAuthCode(HttpServletResponse response, HttpServletRequest request) {
    this.outJson(response, userService.getMenuAuthCode(request), "yyyy-MM-dd");
  }
  @RequestMapping("/queryAllUser")
  @ResponseBody
  public void queryAllUser(HttpServletResponse response, HttpServletRequest request) {
    this.outJson(response, userService.queryAll(), "yyyy-MM-dd");
  }
}
