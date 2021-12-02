package com.sega.application.oa.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.service.system.IModelService;
import com.sega.application.oa.entity.system.ModelEntity;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/apis/model")
public class ModelController extends BaseController {

  private static final Logger log = LoggerFactory.getLogger(ModelController.class);
  /**
   * 注入业务层
   */
  @Autowired
  private IModelService modelService;

  /**
   * 新增或更新数据
   *
   * @param model
   * @return
   */
  @PostMapping("/saveOrUpdate")
  @ResponseBody
  public void saveOrUpdate(@RequestBody ModelEntity model, HttpServletResponse response, HttpServletRequest request) {
    try {
      if (!modelService.checkByModelCode(model)) {
        this.outJson(response, new OutData(false, "编号重复", model), "yyyy-MM-dd");
      } else if (model.getModelId() == null) {
        model.setCreateDate(new Date());
        model.setCreateBy(this.getUserBySession(request).getUserId());
        modelService.saveEntity(model);
        this.outJson(response, new OutData(true, "新增成功", model), "yyyy-MM-dd");
      } else {
        modelService.updateEntity(model);
        model.setUpdateBy(this.getUserBySession(request).getUserId());
        model.setUpdateDate(new Date());
        this.outJson(response, new OutData(true, "更新成功", model), "yyyy-MM-dd");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      if (model.getModelId() == null) {
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
  @DeleteMapping("/delete/{tid}")
  @ResponseBody
  public void delete(@PathVariable Long tid, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
    try {
      ModelEntity model = new ModelEntity();
      model.setModelId(tid);
      model.setModelDel(1);
      model.setUpdateDate(new Date());
      modelService.updateEntity(model);
      this.outJson(response, new OutData(false, "删除成功", model), "yyyy-MM-dd");
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
      List<Long> modelIds = JSONArray.parseArray(tids, Long.class);
      for (Long tid : modelIds) {
        ModelEntity model = new ModelEntity();
        model.setModelId(tid);
        model.setModelDel(1);
        model.setUpdateDate(new Date());
        modelService.updateEntity(model);
      }
      this.outJson(response, new OutData(false, "删除成功"), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      this.outJson(response, new OutData(false, "删除失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 按条件分页查询数据
   *
   * @param model
   * @return
   */
  @PostMapping("/queryByPage")
  @ResponseBody
  public void queryByPage(@RequestBody ModelEntity model, HttpServletResponse response, HttpServletRequest request) {
    try {

      if (model.getPageNum() == null) {
        model.setPageNum(0);
      }
      if (model.getPageSize() == null) {
        model.setPageSize(20);
      }
      PageHelper.startPage(model.getPageNum(), model.getPageSize());
      List<ModelEntity> list = modelService.query(model);
      this.outJson(response, new OutData(true, "查询成功", new PageInfo<ModelEntity>(list)), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(true, "查询失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 按条件查询数据
   *
   * @param model
   * @return
   */
  @PostMapping("/queryAll")
  @ResponseBody
  public void queryAll(@RequestBody ModelEntity model, HttpServletResponse response, HttpServletRequest request) {
    try {
      List<ModelEntity> list = modelService.query(model);
      this.outJson(response, new OutData(true, "查询成功", list), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(false, "查询失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 按条件查询数据
   *
   * @param roleId
   * @return
   */
  @PostMapping("/queryTree")
  @ResponseBody
  public void queryTree(@RequestBody String args, HttpServletResponse response, HttpServletRequest request) {
    try {
      Long roleId = null;
      if (StringUtils.isNotBlank(args) && JSONObject.parseObject(args).get("roleId") != null) {
        roleId = Long.valueOf(JSONObject.parseObject(args).get("roleId").toString());
      }
      List<ModelEntity> list = modelService.queryTree(roleId);
      this.outJson(response, new OutData(true, "查询成功", list), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);

      this.outJson(response, new OutData(false, "查询失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 根据ID获取数据------------
   *
   * @param id
   * @return
   */
  @GetMapping("/get/{id}")
  @ResponseBody
  public void get(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) {
    try {
      this.outJson(response, new OutData(true, "查询成功", modelService.getEntity(id)), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(false, "查询失败"), "yyyy-MM-dd");
    }
  }
}
