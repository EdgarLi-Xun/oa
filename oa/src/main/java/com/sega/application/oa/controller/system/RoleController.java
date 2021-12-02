package com.sega.application.oa.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.service.system.IRoleService;
import com.sega.application.oa.entity.system.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sega.application.oa.entity.system.OutData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Date;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 管理控制层
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2019-10-24 17:02:21<br/>
 * 历史修订：<br/>
 */
@RestController
@CrossOrigin
@RequestMapping("/apis/role")
public class RoleController extends BaseController {

  private static final Logger log = LoggerFactory.getLogger(RoleController.class);
  /**
   * 注入业务层
   */
  @Autowired
  private IRoleService roleService;


  /**
   * 新增或更新数据
   *
   * @param role
   * @return
   */
  @PostMapping("/saveOrUpdate")
  @ResponseBody
  public void saveOrUpdate(@RequestBody RoleEntity role, HttpServletResponse response, HttpServletRequest request) {
    try {
      if (!roleService.checkByRoleName(role)) {
        this.outJson(response, new OutData(false, "角色名重复", role), "yyyy-MM-dd");
      } else if (role.getRoleId() == null) {
        role.setCreateDate(new Date());
        role.setCreateBy(this.getUserBySession(request).getUserId());
        roleService.saveEntity(role);
        this.outJson(response, new OutData(true, "新增成功", role), "yyyy-MM-dd");
      } else {
        roleService.updateEntity(role);
        role.setUpdateBy(this.getUserBySession(request).getUserId());
        role.setUpdateDate(new Date());
        this.outJson(response, new OutData(true, "更新成功", role), "yyyy-MM-dd");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      if (role.getRoleId() == null) {
        this.outJson(response, new OutData(false, "新增失败"), "yyyy-MM-dd");
      } else {
        this.outJson(response, new OutData(false, "更新失败"), "yyyy-MM-dd");
      }
    }
  }

  /**
   * 逻辑删除
   *
   * @param tid
   * @return
   */
  @PostMapping("/delete/{tid}")
  @ResponseBody
  public void delete(@PathVariable Long tid, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
    try {
      RoleEntity role = new RoleEntity();
      role.setRoleId(tid);
      role.setRoleDel(1);
      role.setUpdateDate(new Date());
      roleService.updateEntity(role);
      this.outJson(response, new OutData(false, "删除成功", role), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      this.outJson(response, new OutData(false, "删除失败"), "yyyy-MM-dd");
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
      List<Long> roleIds = JSONArray.parseArray(tids, Long.class);
      for (Long tid : roleIds) {
        RoleEntity role = new RoleEntity();
        role.setRoleId(tid);
        role.setRoleDel(1);
        role.setUpdateDate(new Date());
        roleService.updateEntity(role);
      }
      this.outJson(response, new OutData(false, "删除成功"), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      this.outJson(response, new OutData(false, "删除失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 按条件分页查询数据
   *
   * @param role
   * @return
   */
  @PostMapping("/queryByPage")
  @ResponseBody
  public void queryByPage(@RequestBody RoleEntity role, HttpServletResponse response, HttpServletRequest request) {
    try {

      if (role.getPageNum() == null) {
        role.setPageNum(0);
      }
      if (role.getPageSize() == null) {
        role.setPageSize(20);
      }
      PageHelper.startPage(role.getPageNum(), role.getPageSize());
      List<RoleEntity> list = roleService.query(role);
      this.outJson(response, new OutData(true, "查询成功", new PageInfo<RoleEntity>(list)), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(true, "查询失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 按条件查询数据
   *
   * @param role
   * @return
   */
  @PostMapping("/queryAll")
  @ResponseBody
  public void queryAll(@RequestBody RoleEntity role, HttpServletResponse response, HttpServletRequest request) {
    try {
      List<RoleEntity> list = roleService.query(role);
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
      this.outJson(response, new OutData(true, "查询成功", roleService.getEntity(id)), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(false, "查询失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 权限分配
   *
   * @param modelIds
   * @param roleId
   * @return
   */
  @PostMapping("/permissionAssign")
  @ResponseBody
  public void permissionAssign(String modelIds, Long roleId, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
    try {
      List<Long> modelArray = JSONArray.parseArray("[" + modelIds + "]", Long.class);
      roleService.permissionAssign(modelArray, roleId);
      this.outJson(response, new OutData(true, "分配成功"), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      this.outJson(response, new OutData(false, "分配失败"), "yyyy-MM-dd");
    }
  }
}
