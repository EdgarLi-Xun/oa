package com.sega.application.oa.controller.daily;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.controller.system.BaseController;
import com.sega.application.oa.entity.daily.DailyEntity;
import com.sega.application.oa.entity.daily.DailyfileEntity;
import com.sega.application.oa.entity.daily.dto.DailyDto;
import com.sega.application.oa.entity.daily.vo.DailyEntityVO;
import com.sega.application.oa.entity.system.OutData;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.daily.*;
import com.sega.application.oa.service.system.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 管理控制层
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:30:26<br/>
 * 历史修订：<br/>
 */
@RestController
@CrossOrigin
@RequestMapping("/apis/daily/daily")
public class DailyController extends BaseController {

  private static final Logger log = LoggerFactory.getLogger(DailyController.class);
  /**
   * 注入业务层
   */
  @Autowired
  private IDailyService dailyService;

  @Autowired
  private IUserService userService;


  @Autowired
  private IDailyHazardService dailyHazardService;

  @Autowired
  private IDailyPlanService dailyPlanService;

  @Autowired
  private IDailyDetailService dailyDetailService;

  @Autowired
  private IDailyfileService dailyfileService;

/*
  @Value("${spring.mail.username}")
  private String from;
*/


  @Value("${file.path}")
  private String path;

  /**
   * 新增或更新数据
   *
   * @param daily
   * @return
   */
  @PostMapping("/saveOrUpdate")
  @ResponseBody
  public void saveOrUpdate(@RequestBody DailyEntity daily, HttpServletResponse response, HttpServletRequest request) {
    try {
      if (daily.getDailyId() == null) {
        daily.setDailyCreateDate(new Date());
        daily.setDailyCreateBy(this.getUserBySession(request).getUserId());
        daily.setDayilsjrName(StringUtils.join(daily.getDayilsjrNameArr(), ","));
        daily.setDayilcsrName(StringUtils.join(daily.getDayilcsrNameArr(), ","));
        dailyService.saveEntity(daily);
        this.outJson(response, new OutData(true, "新增成功", daily), "yyyy-MM-dd");
      } else {
        dailyService.updateEntity(daily);
        daily.setDailyUpdateBy(this.getUserBySession(request).getUserId());
        daily.setDailyUpdateDate(new Date());
        daily.setDayilsjrName(StringUtils.join(daily.getDayilsjrNameArr(), ","));
        daily.setDayilcsrName(StringUtils.join(daily.getDayilcsrNameArr(), ","));
        this.outJson(response, new OutData(true, "更新成功", daily), "yyyy-MM-dd");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      if (daily.getDailyId() == null) {
        this.outJson(response, new OutData(false, "新增失败"), "yyyy-MM-dd");
      } else {
        this.outJson(response, new OutData(false, "更新失败"), "yyyy-MM-dd");
      }
    }
  }

  /**
   * 新增或更新数据
   *
   * @return
   */
  @PostMapping("/saveOrUpdateDaily")
  @ResponseBody
  @Transactional(rollbackFor = Exception.class)
  public void saveOrUpdateDaily(@RequestBody DailyEntityVO vo, HttpServletResponse response, HttpServletRequest request) {
    try {

      dailyService.saveOrUpdateAndSend(vo, this.getUserBySession(request).getUserId());

//      DailyEntity daily = jsonObject.getObject("daily", DailyEntity.class);
//      daily.setDailySendDate(new Date());
//      if (daily.getDailyId() == null) {
//        daily.setDailyCreateDate(new Date());
//        daily.setDailyCreateBy(this.getUserBySession(request).getUserId());
//        daily.setDayilsjrName(StringUtils.join(daily.getDayilsjrNameArr(), ","));
//        daily.setDayilcsrName(StringUtils.join(daily.getDayilcsrNameArr(), ","));
//        daily.setDailyDel(0);
//        dailyService.saveEntity(daily);
//      } else {
//        dailyService.updateEntity(daily);
//        daily.setDailyUpdateBy(this.getUserBySession(request).getUserId());
//        daily.setDailyUpdateDate(new Date());
//        daily.setDayilsjrName(StringUtils.join(daily.getDayilsjrNameArr(), ","));
//        daily.setDayilcsrName(StringUtils.join(daily.getDayilcsrNameArr(), ","));
//      }
//      dailyHazardService.deletehazardBydailyid(daily.getDailyId());
//      dailyDetailService.deletedetailBydailyid(daily.getDailyId());
//      dailyPlanService.deleteplanBydailyid(daily.getDailyId());
//
//
//      List<Map<String, Object>> hazardlist = jsonObject.getObject("dailyHazard", List.class);
//      for (Map<String, Object> hamap : hazardlist) {
//        DailyHazardEntity dailyHazardEntity = new DailyHazardEntity();
//        dailyHazardEntity.setDhProblem(hamap.get("dhProblem") == null ? null : hamap.get("dhProblem").toString());
//        dailyHazardEntity.setDhPlan(hamap.get("dhPlan") == null ? null : hamap.get("dhPlan").toString());
//        dailyHazardEntity.setDhDailyId(daily.getDailyId());
//        dailyHazardEntity.setDhCreateBy(this.getUserBySession(request).getUserId());
//        dailyHazardEntity.setDhCreateDate(new Date());
//        dailyHazardEntity.setDhDel(0);
//        dailyHazardService.saveEntity(dailyHazardEntity);
//      }
//      List<Map<String, Object>> planlist = jsonObject.getObject("dailyPlan", List.class);
//      for (Map<String, Object> planmap : planlist) {
//        DailyPlanEntity dailyPlanEntity = new DailyPlanEntity();
//        dailyPlanEntity.setDpDailyId(daily.getDailyId());
//        dailyPlanEntity.setDpProjectId(Long.parseLong(planmap.get("dpProjectId") == "" ? "0" : planmap.get("dpProjectId").toString()));
//        dailyPlanEntity.setDpPlanContent(planmap.get("dpPlanContent") == null ? null : planmap.get("dpPlanContent").toString());
//        dailyPlanEntity.setDpPlanTime((planmap.get("dpPlanTime") == "" || planmap.get("dpPlanTime") == null ? null : planmap.get("dpPlanTime").toString()));
//        dailyPlanEntity.setDpPlanRemarks(planmap.get("dpPlanRemarks") == null ? null : planmap.get("dpPlanRemarks").toString());
//        dailyPlanEntity.setDpCreateBy(this.getUserBySession(request).getUserId());
//        dailyPlanEntity.setDpCreateDate(new Date());
//        dailyPlanEntity.setDpDel(0);
//        dailyPlanService.saveEntity(dailyPlanEntity);
//      }
//      List<Map<String, Object>> detaillist = jsonObject.getObject("dailyDetail", List.class);
//      for (Map<String, Object> demap : detaillist) {
//        DailyDetailEntity dailyDetailEntity = new DailyDetailEntity();
//        dailyDetailEntity.setDdProjectName(demap.get("ddProjectName") == null ? null : demap.get("ddProjectName").toString());
//        dailyDetailEntity.setDdTime((demap.get("ddTime") == "" || demap.get("ddTime") == null ? null : demap.get("ddTime").toString()));
//        dailyDetailEntity.setDdProjectId(Long.parseLong(demap.get("ddProjectId").equals("") ? "0" : demap.get("ddProjectId").toString()));
//        dailyDetailEntity.setDdContent(demap.get("ddContent") == null ? null : demap.get("ddContent").toString());
//        dailyDetailEntity.setDdSchedule(demap.get("ddSchedule") == null ? null : demap.get("ddSchedule").toString());
//        dailyDetailEntity.setDdRemarks(demap.get("ddRemarks") == null ? null : demap.get("ddRemarks").toString());
//        dailyDetailEntity.setDdDailyId(daily.getDailyId());
//        dailyDetailEntity.setDdCreateBy(this.getUserBySession(request).getUserId());
//        dailyDetailEntity.setDdCreateDate(new Date());
//        dailyDetailEntity.setDdDel(0);
//        dailyDetailService.saveEntity(dailyDetailEntity);
//      }
//
//
//      StringBuilder content = new StringBuilder("<html><head></head><body><h3>1.风险问题</h3>");
//      content.append("<table border=\"1\" solid=\"#9999CC\"  cellspacing=\"0\"  style=\"width:1000px;font-size=10px;\">");
//      content.append("<tr style=\"background-color: #DDEBF7; color:#000000\"><th border=\"1\" solid=\"#000000\">序号</th><th border=\"1\" solid=\"#000000\">问题描述</th><th border=\"1\" solid=\"#000000\">应对方案</th></tr>");
//      int i = 1;
//      for (Map<String, Object> hamap : hazardlist) {
//        content.append("<tr>");
//        content.append("<td border=\"1\" solid=\"#000000\"> " + i + "</td>");
//        if (hamap.get("dhProblem") != null && hamap.get("dhProblem") != "") {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + hamap.get("dhProblem") + "</td>");
//        } else {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + "" + "</td>");
//        }
//        if (hamap.get("dhPlan") != null && hamap.get("dhPlan") != "") {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + hamap.get("dhPlan") + "</td>");
//        } else {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + "" + "</td>");
//        }
//        content.append("</tr>");
//        i++;
//      }
//
//      content.append("</table>");
//      content.append("<br></br>");
//      content.append("<h3>2.工作进展</h3>");
//      content.append("<table border=\"1\"solid=\"#9999CC\" cellspacing=\"0\" style=\"width:1000px;font-size=10px;\">");
//      content.append("<tr style=\"background-color: #DDEBF7; color:#000000\"><th border=\"1\" solid=\"#000000\">序号</th><th border=\"1\" solid=\"#000000\">工作内容</th><th border=\"1\" solid=\"#000000\">进度</th><th border=\"1\" solid=\"#000000\">耗时</th><th border=\"1\" solid=\"#000000\">备注</th></tr>");
//
//      int k = 1;
//
//
//      for (Map<String, Object> demap : detaillist) {
//        content.append("<tr>");
//        content.append("<td border=\"1\" solid=\"#000000\"> " + k + "</td>");
//        if (demap.get("ddProjectName") != null && demap.get("ddProjectName") != "") {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + demap.get("ddContent") + "[" + demap.get("ddProjectName") + "]" + "</td>");
//        } else {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + demap.get("ddContent") + "</td>");
//        }
//
//        if (demap.get("ddSchedule") != null && demap.get("ddSchedule") != "") {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + demap.get("ddSchedule") + "%</td>");
//        } else {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + "" + "</td>");
//        }
//
//        if (demap.get("ddTime") != null && demap.get("ddTime") != "") {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + demap.get("ddTime") + "</td>");
//        } else {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + "" + "</td>");
//        }
//
//        if (demap.get("ddRemarks") != null && demap.get("ddRemarks") != "") {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + demap.get("ddRemarks") + "</td>");
//        } else {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + "" + "</td>");
//        }
//
//        content.append("</tr>");
//        k++;
//
//      }
//      int j = 1;
//
//
//      content.append("</table>");
//      content.append("<br></br>");
//      content.append("<h3>3.明日计划</h3>");
//      content.append("<table border=\"1\" solid=\"#9999CC\" cellspacing=\"0\" style=\"width:1000px;font-size=10px;\">");
//      content.append("<tr style=\"background-color: #DDEBF7; color:#000000\"><th border=\"1\" solid=\"#000000\">序号</th><th border=\"1\" solid=\"#000000\">内容</th><th>预期完成时间</th><th border=\"1\" solid=\"#000000\">备注</th></tr>");
//
//      for (Map<String, Object> planmap : planlist) {
//        content.append("<tr>");
//        content.append("<td border=\"1\" solid=\"#000000\"> " + j + "</td>");
//        if (planmap.get("dpPlanContent") == null) {
//          planmap.put("dpPlanContent", "");
//        }
//        if (planmap.get("ddProjectName") != null && planmap.get("ddProjectName") != "") {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + planmap.get("dpPlanContent") + "[" + planmap.get("dpProjectName") + "]" + "</td>");
//        } else {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + planmap.get("dpPlanContent") + "</td>");
//        }
//        if (planmap.get("dpPlanTime") != null && planmap.get("dpPlanTime") != "") {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + planmap.get("dpPlanTime") + "</td>");
//        } else {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + "" + "</td>");
//        }
//        //第一列
//        if (planmap.get("dpPlanRemarks") != null && planmap.get("dpPlanRemarks") != "") {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + planmap.get("dpPlanRemarks") + "</td>");
//        } else {
//          content.append("<td border=\"1\" solid=\"#000000\"> " + "" + "</td>");
//        }
//        content.append("</tr>");
//        j++;
//      }
//
//      content.append("</table>");
//      content.append("</body></html>");
//
//
//      UserEntity entity = (UserEntity) userService.getEntity(daily.getDayilUserId());
//      // 配置消息基础信息
//      if(org.springframework.util.StringUtils.isEmpty(entity.getEmailPassword())){
//        throw new Exception("邮箱密码为空");
//      }
//      SendMailBasePropertyConfig mailConfig = new SendMailBasePropertyConfig(entity.getUserEmail(),entity.getEmailPassword());
//      JavaMailSenderImpl jms = mailConfig.createJMS();
//
//      MimeMessage message = jms.createMimeMessage();
//      //true表示需要创建一个multipart message
//      MimeMessageHelper helper = null;
//      String sjr = daily.getDayilsjrName();
//      try {
//        helper = new MimeMessageHelper(message, true);
//
//        helper.setFrom(entity.getUserEmail());
//
//        String[] sjrs = sjr.split(",");
//        helper.setTo(sjrs);
//        if (daily.getDayilcsrName() != null && daily.getDayilcsrName() != "") {
//          String csr = daily.getDayilcsrName();
//          String[] csrs = csr.split(",");
//          helper.setCc(csrs);
//        }
//        helper.setSubject(daily.getDayilUserName() + daily.getDailyTheme());
//        helper.setText(content.toString(), true);
//
//        // 系统文件
//        DailyfileEntity fileEntityold = new DailyfileEntity();
//        String dailyFile = daily.getDailyFile();
//        if(org.springframework.util.StringUtils.hasLength(dailyFile) ){
//          String[] dailyFileIds = dailyFile.split(",");
//          for (int z = 0; z < dailyFileIds.length; z++) {
//            fileEntityold.setDailyfileDailyid(daily.getDailyId());
//            fileEntityold.setDailyfileId(Long.valueOf(dailyFileIds[z]));
//            dailyfileService.updateEntity(fileEntityold);
//            // 查询数据
//            DailyfileEntity serviceEntity = (DailyfileEntity)dailyfileService.getEntity(Long.valueOf(dailyFileIds[z]));
//            helper.addAttachment(new File(serviceEntity.getDailyfilePath()).getName(), new File(serviceEntity.getDailyfilePath()));
//          }
//        }
//        // 发送信息
//        jms.send(message);
//        this.outJson(response, new OutData(true, "新增成功", daily), "yyyy-MM-dd");
//      } catch (MessagingException e) {
//        this.outJson(response, new OutData(false, "消息发送失败"), "yyyy-MM-dd");
//      }
      this.outJson(response, new OutData(true, "新增成功"), "yyyy-MM-dd");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(false, e.getMessage()), "yyyy-MM-dd");

    }
  }

  /**
   * 根据日报id查询日报详情
   *
   * @param dailyid
   * @return
   */
  @PostMapping("/querydailyFileBydailyid")
  @ResponseBody
  public void querydailyFileBydailyid(@RequestBody String dailyid, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
    String daid = dailyid.replaceAll("=", "");
    DailyfileEntity dailyfileEntity = new DailyfileEntity();
    dailyfileEntity.setDailyfileDailyid(Long.parseLong(daid));
    List<DailyfileEntity> list = dailyfileService.query(dailyfileEntity);
    this.outJson(response, new OutData(true, "查询成功", list), "yyyy-MM-dd");
  }

  /**
   * 下载文件
   */
  @RequestMapping("/downLoadFile/{id}")
  public void downFile(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) {
    if (org.springframework.util.StringUtils.isEmpty(id)) {
      this.outJson(response, new OutData(false, "参数出错"));
    }
    DailyfileEntity entity = (DailyfileEntity) dailyfileService.getEntity(id);
    String localPath = entity.getDailyfilePath();
    // 加载文件
    File file = new File(localPath);
    BufferedInputStream in = null;
    ServletOutputStream os = null;
    if (!file.exists() || file.isDirectory()) {
      this.outJson(response, new OutData(false, "文件不存在"));
    }
    // 设置文件 contentType类型 自动判断下载
    response.setContentType("multipart/form-data");
    // 设置下载文件名称
    try {
      response.setHeader("Content-Disposition", "attachment;filename=" + new String(entity.getDailyfileName().getBytes("utf-8"), "iso-8859-1"));
      response.setCharacterEncoding("utf-8");
      in = new BufferedInputStream(new FileInputStream(file));
      os = response.getOutputStream();
      IOUtils.copy(in, os);
      os.flush();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (in != null) {
          in.close();
        }
        if (os != null) {
          os.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
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
      DailyEntity daily = new DailyEntity();
      daily.setDailyId(tid);
      daily.setDailyDel(1);
      daily.setDailyUpdateDate(new Date());
      dailyService.updateEntity(daily);
      this.outJson(response, new OutData(false, "删除成功", daily), "yyyy-MM-dd");
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
      List<Long> ids = JSONArray.parseArray(tids, Long.class);
      for (Long tid : ids) {
        DailyEntity daily = new DailyEntity();
        daily.setDailyId(tid);
        daily.setDailyDel(1);
        daily.setDailyUpdateDate(new Date());
        dailyService.updateEntity(daily);
      }
      this.outJson(response, new OutData(false, "删除成功"), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      this.outJson(response, new OutData(false, "删除失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 按条件分页查询数据
   *
   * @param
   * @return
   */
  @PostMapping("/queryByPage")
  @ResponseBody
  public void queryByPage(@RequestBody DailyEntity condition, HttpServletResponse response, HttpServletRequest request) {
    try {
      DailyEntity daily = new DailyEntity();
      BeanUtils.copyProperties(condition, daily);
      if (daily.getPageNum() == null) {
        daily.setPageNum(0);
      }
      if (daily.getPageSize() == null) {
        daily.setPageSize(20);
      }
      PageHelper.startPage(daily.getPageNum(), daily.getPageSize());
      List<DailyEntity> list = dailyService.query(daily);
      if (list != null && list.size() > 0) {
        for (DailyEntity de : list) {
          if (StringUtils.isNotEmpty(de.getDayilsjrName())) {
            de.setDayilsjrNameArr(de.getDayilsjrName().split(","));
          }
          if (StringUtils.isNotEmpty(de.getDayilcsrName())) {
            de.setDayilcsrNameArr(de.getDayilcsrName().split(","));
          }
        }
      }
      this.outJson(response, new OutData(true, "查询成功", new PageInfo<DailyEntity>(list)), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(true, "查询失败"), "yyyy-MM-dd");
    }
  }


  /**
   * 按条件分页查询下属数据
   *
   * @param
   * @return
   */
  @PostMapping("/queryByPageXs")
  @ResponseBody
  public void queryByPageXs(@RequestBody DailyEntity condition, HttpServletResponse response, HttpServletRequest request) {
    try {
      DailyEntity daily = new DailyEntity();
      BeanUtils.copyProperties(condition, daily);
      //根据登录用户id拿到部门编号
      UserEntity byUserId = userService.getByUserId(condition.getDayilUserId());
      daily.setDayilUserId(byUserId.getUserDepartmentId());
      if (daily.getPageNum() == null) {
        daily.setPageNum(0);
      }
      if (daily.getPageSize() == null) {
        daily.setPageSize(20);
      }
      PageHelper.startPage(daily.getPageNum(), daily.getPageSize());

      //查询当前登录用户所属部门下日报数据
      List<DailyEntity> list = dailyService.queryXs(daily);
      if (list != null && list.size() > 0) {
        for (DailyEntity de : list) {
          if (StringUtils.isNotEmpty(de.getDayilsjrName())) {
            de.setDayilsjrNameArr(de.getDayilsjrName().split(","));
          }
          if (StringUtils.isNotEmpty(de.getDayilcsrName())) {
            de.setDayilcsrNameArr(de.getDayilcsrName().split(","));
          }
        }
      }
      this.outJson(response, new OutData(true, "查询成功", new PageInfo<DailyEntity>(list)), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(true, "查询失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 按条件查询数据
   *
   * @param daily
   * @return
   */
  @PostMapping("/queryAll")
  @ResponseBody
  public void queryAll(@RequestBody DailyEntity daily, HttpServletResponse response, HttpServletRequest request) {
    try {
      List<DailyEntity> list = dailyService.query(daily);
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
      Object entity = dailyService.getEntity(id);
      DailyDto dailyDto = new DailyDto();
      BeanUtils.copyProperties(entity, dailyDto);

      SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      dailyDto.setSendTime(sdf.format(dailyDto.getDailySendDateTm()));
      this.outJson(response, new OutData(true, "查询成功", dailyDto), "yyyy-MM-dd");
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(false, "查询失败"), "yyyy-MM-dd");
    }
  }

  /**
   * 插入excel获取数据
   */
  @RequestMapping("/importdaily")
  @ResponseBody
  public void importdaily(MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
    try {
      String fileSep = System.getProperty("\\");
      String[] str1 = file.getOriginalFilename().split("\\.");
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      String[] date = simpleDateFormat.format(new Date()).split("-");
      File dest = new File(path + file.getOriginalFilename());
      File file1 = new File(path);
      if (!file1.exists()) {
        file1.mkdirs();
        File file2 = new File(path + fileSep + date[0] + fileSep + date[1]);
        if (!file2.exists()) {
          file2.mkdirs();
          File file3 = new File(path + fileSep + date[0] + fileSep + date[1] + fileSep + date[2]);
          if (!file3.exists()) {
            file3.mkdirs();
          }
        }
      }
      if (!dest.exists()) {
        boolean cf = dest.createNewFile();
        if (cf) {
          file.transferTo(dest);//MultipartFile能自动转存文件
          //other action
        }
      }
    /*  DailyfileEntity fileEntityold = dailyfileService.querydailyfile();
      if (fileEntityold != null) {
        dailyfileService.deleteById(fileEntityold.getDailyfileId());
      }*/

      DailyfileEntity fileEntity = new DailyfileEntity();
      fileEntity.setDailyfileDel(0);
      fileEntity.setDailyfilePath(dest.getCanonicalPath());
      fileEntity.setDailyfileName(file.getOriginalFilename());
      fileEntity.setDailyfileRealName(new Date().getTime() + file.getOriginalFilename());
      fileEntity.setDailyfileCreateDate(new Date());
      fileEntity.setDailyfileType(str1[str1.length - 1]);
      dailyfileService.saveEntity(fileEntity);
      this.outJson(response, new OutData(true, "上传成功", fileEntity), "yyyy-MM-dd");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      this.outJson(response, new OutData(false, "上传失败"), "yyyy-MM-dd");
    }
  }
}
