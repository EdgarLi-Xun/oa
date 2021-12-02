package com.sega.application.oa.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.entity.system.DepartmentEntity;
import com.sega.application.oa.entity.system.OutData;
import com.sega.application.oa.service.system.IDepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 创建日期：2019-10-23 15:05:26<br/>
 * 历史修订：<br/>
 */
@RestController
@CrossOrigin
@RequestMapping("/apis/department")
public class DepartmentController extends BaseController {

  private static final Logger log = LoggerFactory.getLogger(DepartmentController.class);
  /**
   * 注入业务层
   */
  @Autowired
  private IDepartmentService departmentService;

  /**
   * 新增或更新数据
   *
   * @param department
   * @return
   */
  @PostMapping("/saveOrUpdate")
  @ResponseBody
  public void saveOrUpdate(@RequestBody DepartmentEntity department, HttpServletResponse response, HttpServletRequest request) {
    try {
      if (!departmentService.checkByDepartmentCode(department)) {
        this.outJson(response, new OutData(false, "部门编号重复", department), "yyyy-MM-dd");
      } else if (department.getDepartmentId() == null) {
        department.setCreateDate(new Date());
        department.setCreateBy(this.getUserBySession(request).getUserId());
        departmentService.saveEntity(department);
        this.outJson(response, new OutData(true, "新增成功", department), "yyyy-MM-dd");
      } else {
        departmentService.updateEntity(department);
        department.setUpdateBy(this.getUserBySession(request).getUserId());
        department.setUpdateDate(new Date());
        this.outJson(response, new OutData(true, "更新成功", department), "yyyy-MM-dd");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      if (department.getDepartmentId() == null) {
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
      DepartmentEntity department = new DepartmentEntity();
      department.setDepartmentId(tid);
      department.setDepartmentDel(1);
      department.setUpdateDate(new Date());
      departmentService.updateEntity(department);
      this.outJson(response, new OutData(false, "删除成功", department), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      this.outJson(response, new OutData(false, "删除失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 按条件分页查询数据
   *
   * @param department
   * @return
   */
  @PostMapping("/queryByPage")
  @ResponseBody
  public void queryByPage(@RequestBody DepartmentEntity department, HttpServletResponse response, HttpServletRequest request) {
    try {
      if (department.getPageNum() == null) {
        department.setPageNum(0);
      }
      if (department.getPageSize() == null) {
        department.setPageSize(20);
      }
      PageHelper.startPage(department.getPageNum(), department.getPageSize());
      List<DepartmentEntity> list = departmentService.query(department);
      this.outJson(response, new OutData(true, "查询成功", new PageInfo<DepartmentEntity>(list)), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(true, "查询失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 按条件查询数据
   *
   * @param department
   * @return
   */
  @PostMapping("/queryAll")
  @ResponseBody
  public void queryAll(@RequestBody DepartmentEntity department, HttpServletResponse response, HttpServletRequest request) {
    try {
      List<DepartmentEntity> list = departmentService.query(department);
      this.outJson(response, new OutData(true, "查询成功", list), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(false, "查询失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 按条件查询数据
   *
   * @return
   */
  @PostMapping("/queryTreeData")
  @ResponseBody
  public void queryTreeData(HttpServletResponse response, HttpServletRequest request) {
    try {
      List<DepartmentEntity> list = departmentService.queryTreeData();
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
      this.outJson(response, new OutData(true, "查询成功", departmentService.getEntity(id)), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(false, "查询失败"), "yyyy-MM-dd");
    }
  }
}
