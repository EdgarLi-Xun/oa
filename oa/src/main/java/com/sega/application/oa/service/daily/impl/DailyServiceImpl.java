package com.sega.application.oa.service.daily.impl;

import com.sega.application.oa.config.SendMailBasePropertyConfig;
import com.sega.application.oa.dao.daily.IDailyDao;
import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.entity.daily.*;
import com.sega.application.oa.entity.daily.vo.DailyEntityVO;
import com.sega.application.oa.entity.system.DepartmentEntity;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.daily.*;
import com.sega.application.oa.service.system.IDepartmentService;
import com.sega.application.oa.service.system.IUserService;
import com.sega.application.oa.service.system.SendSmsService;
import com.sega.application.oa.service.system.impl.BaseServiceImpl;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 管理持久化层
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:25:28<br/>
 * 历史修订：<br/>
 */
@Service
public class DailyServiceImpl extends BaseServiceImpl implements IDailyService {

  @Autowired
  private IDailyDao dailyDao;

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

  @Autowired
  private IUserService iUserService;

  @Autowired
  private IDepartmentService iDepartmentService;

  @Autowired
  private SendSmsService sendSmsService;

  @Value("${sega.dwdz}")
  private String dwdz;

  @Value("${sega.dwgh}")
  private String dwgh;

  @Override
  public List<DailyEntity> queryXs(DailyEntity daily) {
    return dailyDao.queryXs(daily);
  }

  @Override
  protected IBaseDao getDao() {
    return dailyDao;
  }

  @Override
  public List<Object> queryList(DailyEntityVO vo) {
    return dailyDao.queryList(vo);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void saveOrUpdateAndSend(DailyEntityVO vo, Long userId) throws Exception {
    DailyEntity daily = vo.getDaily();
    if (vo.isSende()) {
      daily.setDailySendTy(1);
    } else {
      daily.setDailySendTy(0);
    }
    if (daily.getDailyId() == null) {
      daily.setDailyCreateBy(userId);
//      daily.setDayilsjrName(StringUtils.join(daily.getDayilsjrNameArr(), ","));
//      daily.setDayilcsrName(StringUtils.join(daily.getDayilcsrNameArr(), ","));
      daily.setDailyDel(0);
      this.saveEntity(daily);
    } else {
      daily.setDailyUpdateBy(userId);
      daily.setDailyUpdateDate(new Date());
//      daily.setDayilsjrName(StringUtils.join(daily.getDayilsjrNameArr(), ","));
//      daily.setDayilcsrName(StringUtils.join(daily.getDayilcsrNameArr(), ","));
      this.updateEntity(daily);
    }

    dailyHazardService.deletehazardBydailyid(daily.getDailyId());
    dailyDetailService.deletedetailBydailyid(daily.getDailyId());
    dailyPlanService.deleteplanBydailyid(daily.getDailyId());

    //批量保存风险
    List<DailyHazardEntity> dailyHazardList = vo.getDailyHazard();
    dailyHazardList.forEach(item -> {
      item.setDhDailyId(daily.getDailyId());
      item.setDhCreateBy(userId);
      item.setDhCreateDate(new Date());
      item.setDhDel(0);
    });

    for (DailyHazardEntity dailyHazardEntity : dailyHazardList) {
      dailyHazardService.saveEntity(dailyHazardEntity);
    }
    //批量保存工作进展
    List<DailyDetailEntity> dailyDetailList = vo.getDailyDetail();
    if (dailyDetailList == null || dailyDetailList.isEmpty()) {
      throw new RuntimeException("今日工作内容不能为空！");
    }
    dailyDetailList.forEach(item -> {
      item.setDdDailyId(daily.getDailyId());
      item.setDdCreateBy(userId);
      item.setDdCreateDate(new Date());
      item.setDdDel(0);
    });
    for (DailyDetailEntity dailyDetailEntity : dailyDetailList) {
      dailyDetailService.saveEntity(dailyDetailEntity);
    }
    //批量保存风险
    List<DailyPlanEntity> dailyPlanList = vo.getDailyPlan();
    if (dailyPlanList == null || dailyPlanList.isEmpty()) {
      throw new RuntimeException("明日计划不能为空！");
    }
    dailyPlanList.forEach(item -> {
      item.setDpDailyId(daily.getDailyId());
      item.setDpCreateBy(userId);
      item.setDpCreateDate(new Date());
      item.setDpDel(0);
    });
    for (DailyPlanEntity dailyPlanEntity : dailyPlanList) {
      dailyPlanService.saveEntity(dailyPlanEntity);
    }
    List<DailyfileEntity> dailyfileEntityList = dailyfileService.getByDailyid(daily.getDailyId());


    // 系统文件
    DailyfileEntity fileEntityold = new DailyfileEntity();
    String dailyFile = daily.getDailyFile();
    Map<Long, Boolean> map = new HashMap<>();
    if (org.springframework.util.StringUtils.hasLength(dailyFile)) {
      String[] dailyFileIds = dailyFile.split(",");
      for (int z = 0; z < dailyFileIds.length; z++) {
        fileEntityold.setDailyfileDailyid(daily.getDailyId());
        fileEntityold.setDailyfileId(Long.valueOf(dailyFileIds[z]));
        map.put(Long.valueOf(dailyFileIds[z]), true);
        dailyfileService.updateEntity(fileEntityold);
      }
    }

    //修改删除不同的
    for (DailyfileEntity dailyfileEntity : dailyfileEntityList) {
      if (!map.containsKey(dailyfileEntity.getDailyfileId())) {
        dailyfileService.deleteEntity(dailyfileEntity.getDailyfileId());
      }
    }
    if (vo.isSende()) {
      send(daily, dailyHazardList, dailyDetailList, dailyPlanList, userId);
    }

  }


  private void send(DailyEntity daily, List<DailyHazardEntity> hazardlist, List<DailyDetailEntity> detaillist, List<DailyPlanEntity> planlist, Long userId) throws Exception {

    UserEntity userEntity = iUserService.getByUserId(userId);
    DepartmentEntity departmentEntity = iDepartmentService.getEntityById(userEntity.getUserDepartmentId());
    StringBuilder content = new StringBuilder("<html><head></head><body><h3>1.风险问题</h3>");
    content.append("<table border=\"1\" solid=\"#9999CC\"  cellspacing=\"0\"  style=\"width:1100px;font-size=11px;text-align:center;\">");
    content.append("<tr style=\"background-color: #DDEBF7; color:#000000\">" + "<th border=\"1\" solid=\"#000000\">序号</th>" + "<th border=\"1\" solid=\"#000000\">问题描述</th>" + "<th border=\"1\" solid=\"#000000\">应对方案</th></tr>");
    int i = 1;
    for (DailyHazardEntity hamap : hazardlist) {
      content.append("<tr>");
      content.append("<td border=\"1\" solid=\"#000000\" style=\" text-align: center;width:100px;\"> " + i + "</td>");
      content.append("<td border=\"1\" solid=\"#000000\" style=\" text-align: left;width:500px;\"> " + hamap.getDhProblem() + "</td>");

      content.append("<td border=\"1\" solid=\"#000000\" style=\" text-align: left;width:500px;\"> " + hamap.getDhPlan() + "</td>");

      content.append("</tr>");
      i++;
    }
    content.append("</table>");

    content.append("<h3>2.工作进展</h3>");
    content.append("<table border=\"1\"solid=\"#9999CC\" cellspacing=\"0\" style=\"width:1100px;font-size=11px;\">");
    content.append("<tr style=\"background-color: #DDEBF7; color:#000000\">" + "<th border=\"1\" solid=\"#000000\">序号</th>" + "<th border=\"1\" solid=\"#000000\">项目</th>" + "<th border=\"1\" solid=\"#000000\">工作内容</th>" + "<th border=\"1\" solid=\"#000000\">进度</th>" + "<th border=\"1\" solid=\"#000000\">耗时</th>" + "<th border=\"1\" solid=\"#000000\">备注</th></tr>");

    int k = 1;


    for (DailyDetailEntity demap : detaillist) {
      content.append("<tr>");
      content.append("<td border=\"1\" solid=\"#000000\" style=\" text-align: center;width:100px;\"> " + k + "</td>");
      content.append("<td border=\"1\" solid=\"#000000\" style=\" text-align: center;width:200px;\"> " + demap.getDdProjectName() + "</td>");
      content.append("<td border=\"1\" solid=\"#000000\" style=\" text-align: left;width:400px;\"> " + demap.getDdContent() + "</td>");
      content.append("<td border=\"1\" solid=\"#000000\" style=\" text-align: center;width:100px;\"> " + demap.getDdSchedule() + "%</td>");
      content.append("<td border=\"1\" solid=\"#000000\" style=\" text-align: center;width:100px;\"> " + demap.getDdTime() + "</td>");
      content.append("<td border=\"1\" solid=\"#000000\" style=\" text-align: left;width:200px;\"> " + demap.getDdRemarks() + "</td>");

      content.append("</tr>");
      k++;

    }
    int j = 1;
    content.append("</table>");

    content.append("<h3>3.明日计划</h3>");
    content.append("<table border=\"1\" solid=\"#9999CC\" cellspacing=\"0\" style=\"width:1100px;font-size=11px;\">");
    content.append("<tr style=\"background-color: #DDEBF7; color:#000000\">" + "<th border=\"1\" solid=\"#000000\">序号</th>" + "<th border=\"1\" solid=\"#000000\">项目</th>" + "<th border=\"1\" solid=\"#000000\">内容</th>" + "<th border=\"1\" solid=\"#000000\">预期完成时间</th>" + "<th border=\"1\" solid=\"#000000\">备注</th></tr>");

    for (DailyPlanEntity planmap : planlist) {
      content.append("<tr>");
      content.append("<td border=\"1\" solid=\"#000000\" style=\" text-align: center;width:100px;\"> " + j + "</td>");
      content.append("<td border=\"1\" solid=\"#000000\" style=\" text-align: center;width:200px;\"> " + planmap.getDpProjectName() + "</td>");
      content.append("<td border=\"1\" solid=\"#000000\" style=\" text-align: left;width:400px;\"> " + planmap.getDpPlanContent() + "</td>");
      content.append("<td border=\"1\" solid=\"#000000\" style=\" text-align: center;width:200px;\"> " + planmap.getDpPlanTime() + "</td>");
      //第一列
      content.append("<td border=\"1\" solid=\"#000000\" style=\" text-align: left;width:200px;\"> " + planmap.getDpPlanRemarks() + "</td>");

      content.append("</tr>");
      j++;
    }

    content.append("</table>");
    content.append("<div style=\"padding-left: 10px;\">\n" + "    <br/><br/><br/>\n" + "    <br/><br/><br/>\n" + "    <br/><br/><br/>\n" + "    <br/><br/><br/>\n" + "    <hr style='background-color: #B5C4DF; height: 1px; width: 200px; border: none; text-align: left; margin-left: 0;' />\n" + "    <br/>\n" + "    祝  诸事顺意！\n" + "    <br/><br/>\n" + "    <hr style='background-color: #2E60B7; height: 1px; width: 200px; border: none; text-align: left; margin-left: 0;' />\n" + "    <img height='66' width='66' src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAARAAAAEYCAYAAACUWal4AAAACXBIWXMAAAsTAAALEwEAmpwYAAAL4WlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDUgNzkuMTYzNDk5LCAyMDE4LzA4LzEzLTE2OjQwOjIyICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0RXZ0PSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VFdmVudCMiIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczpwaG90b3Nob3A9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGhvdG9zaG9wLzEuMC8iIHhtbG5zOnRpZmY9Imh0dHA6Ly9ucy5hZG9iZS5jb20vdGlmZi8xLjAvIiB4bWxuczpleGlmPSJodHRwOi8vbnMuYWRvYmUuY29tL2V4aWYvMS4wLyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgKFdpbmRvd3MpIiB4bXA6Q3JlYXRlRGF0ZT0iMjAxOC0wNC0yMFQwOTowNzozMSswODowMCIgeG1wOk1ldGFkYXRhRGF0ZT0iMjAxOS0wNC0wM1QwODo1NjozMiswODowMCIgeG1wOk1vZGlmeURhdGU9IjIwMTktMDQtMDNUMDg6NTY6MzIrMDg6MDAiIGRjOmZvcm1hdD0iaW1hZ2UvcG5nIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOmM5NGRjYTA4LWQ5ZTktNzg0YS04YTQ5LTBiODdiMjBiYWYzZSIgeG1wTU06RG9jdW1lbnRJRD0iYWRvYmU6ZG9jaWQ6cGhvdG9zaG9wOmYyODllMzliLTczN2YtNjg0ZS1hZTQ4LTE3YmZjYjI0Y2E2MCIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjFiODMzMWFhLTg5NTgtMjg0ZS04MDk0LTRjYjEzYjFmNjgyZCIgcGhvdG9zaG9wOkNvbG9yTW9kZT0iMyIgdGlmZjpPcmllbnRhdGlvbj0iMSIgdGlmZjpYUmVzb2x1dGlvbj0iNzIwMDAwLzEwMDAwIiB0aWZmOllSZXNvbHV0aW9uPSI3MjAwMDAvMTAwMDAiIHRpZmY6UmVzb2x1dGlvblVuaXQ9IjIiIGV4aWY6Q29sb3JTcGFjZT0iNjU1MzUiIGV4aWY6UGl4ZWxYRGltZW5zaW9uPSIxOTIwIiBleGlmOlBpeGVsWURpbWVuc2lvbj0iMTA4MCI+IDx4bXBNTTpIaXN0b3J5PiA8cmRmOlNlcT4gPHJkZjpsaSBzdEV2dDphY3Rpb249ImNyZWF0ZWQiIHN0RXZ0Omluc3RhbmNlSUQ9InhtcC5paWQ6MWI4MzMxYWEtODk1OC0yODRlLTgwOTQtNGNiMTNiMWY2ODJkIiBzdEV2dDp3aGVuPSIyMDE4LTA0LTIwVDA5OjA3OjMxKzA4OjAwIiBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBQaG90b3Nob3AgQ0MgKFdpbmRvd3MpIi8+IDxyZGY6bGkgc3RFdnQ6YWN0aW9uPSJzYXZlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDoxZTlmYzUxOC1hZTQyLTBiNDUtODhiMi05NmVjNWU1M2QwZjYiIHN0RXZ0OndoZW49IjIwMTgtMDQtMjBUMDk6MDg6MTYrMDg6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAoV2luZG93cykiIHN0RXZ0OmNoYW5nZWQ9Ii8iLz4gPHJkZjpsaSBzdEV2dDphY3Rpb249InNhdmVkIiBzdEV2dDppbnN0YW5jZUlEPSJ4bXAuaWlkOmE1ODQ1NDUxLTJlNTctOTQ0NC04YTM4LThlNGU0NjBkMTdlZiIgc3RFdnQ6d2hlbj0iMjAxOS0wNC0wM1QwODo1NjozMiswODowMCIgc3RFdnQ6c29mdHdhcmVBZ2VudD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTkgKFdpbmRvd3MpIiBzdEV2dDpjaGFuZ2VkPSIvIi8+IDxyZGY6bGkgc3RFdnQ6YWN0aW9uPSJjb252ZXJ0ZWQiIHN0RXZ0OnBhcmFtZXRlcnM9ImZyb20gYXBwbGljYXRpb24vdm5kLmFkb2JlLnBob3Rvc2hvcCB0byBpbWFnZS9wbmciLz4gPHJkZjpsaSBzdEV2dDphY3Rpb249ImRlcml2ZWQiIHN0RXZ0OnBhcmFtZXRlcnM9ImNvbnZlcnRlZCBmcm9tIGFwcGxpY2F0aW9uL3ZuZC5hZG9iZS5waG90b3Nob3AgdG8gaW1hZ2UvcG5nIi8+IDxyZGY6bGkgc3RFdnQ6YWN0aW9uPSJzYXZlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDpjOTRkY2EwOC1kOWU5LTc4NGEtOGE0OS0wYjg3YjIwYmFmM2UiIHN0RXZ0OndoZW49IjIwMTktMDQtMDNUMDg6NTY6MzIrMDg6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE5IChXaW5kb3dzKSIgc3RFdnQ6Y2hhbmdlZD0iLyIvPiA8L3JkZjpTZXE+IDwveG1wTU06SGlzdG9yeT4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6YTU4NDU0NTEtMmU1Ny05NDQ0LThhMzgtOGU0ZTQ2MGQxN2VmIiBzdFJlZjpkb2N1bWVudElEPSJhZG9iZTpkb2NpZDpwaG90b3Nob3A6NzExODA2ZTctYjU5NC00NjQ4LTkzMTEtYWU3MDZmZDQxYTg5IiBzdFJlZjpvcmlnaW5hbERvY3VtZW50SUQ9InhtcC5kaWQ6MWI4MzMxYWEtODk1OC0yODRlLTgwOTQtNGNiMTNiMWY2ODJkIi8+IDxwaG90b3Nob3A6VGV4dExheWVycz4g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\">\n" + "    <br/>\n" + "    南京释加软件科技有限公司\n" + "    <br/>\n" + "    " + userEntity.getUserName() + "   |   " + userEntity.getUserLoginName() + "\n" + "    <br/>\n" + "    部门 ：" + departmentEntity.getDepartmentName() + "\n" + "    <br/>\n" + "    手机 ：" + userEntity.getUserTelephone() + "\n" + "    <br/>\n" + "    固话 ：" + dwgh + "\n" + "    <br/>\n" + "    地址 ：" + dwdz + "\n" + "    <br/>\n" + "    <br/>\n" + "    <DIV style=\"BORDER-TOP: #00686b 1px dashed; OVERFLOW: hidden; HEIGHT: 1px\"></DIV>\n" + "    <br/>\n" + "   <span> 敬启：本邮件仅供邮件地址所列出的个人或群组使用，其所载内容可能因含有\n" + "    南京释加软件科技有限公司的保密信息或其它原因而不得泄露。除发件人及指\n" + "    定收件人之外的任何人不可传播、分发、复制、印刷或者使用本邮件中所含信\n" + "    息的全部或部分。若您误收到本邮件，请立即通知发件人，并将原始邮件、附\n" + "    件及其所有副本从系统中删除，切勿使用！</span>\n" + "\n" + "</div>");
    content.append("</body></html>");

    UserEntity entity = (UserEntity) userService.getEntity(daily.getDayilUserId());

    if (org.springframework.util.StringUtils.isEmpty(entity.getEmailPassword())) {
      throw new Exception("邮箱密码为空");
    }
    SendMailBasePropertyConfig mailConfig = new SendMailBasePropertyConfig(entity.getUserEmail(), entity.getEmailPassword());
    JavaMailSenderImpl jms = mailConfig.createJMS();

    MimeMessage message = jms.createMimeMessage();
    //true表示需要创建一个multipart message
    MimeMessageHelper helper = null;
    String sjr = daily.getDayilsjrName();
    helper = new MimeMessageHelper(message, true);

    helper.setFrom(entity.getUserEmail());

    String[] sjrs = sjr.split(",");
    helper.setTo(sjrs);
    if (daily.getDayilcsrName() != null && daily.getDayilcsrName() != "") {
      String csr = daily.getDayilcsrName();
      String[] csrs = csr.split(",");
      helper.setCc(csrs);
    }
    helper.setSubject(daily.getDayilUserName() + daily.getDailyTheme());
    helper.setText(content.toString(), true);

    // 系统文件
    DailyfileEntity fileEntityold = new DailyfileEntity();
    String dailyFile = daily.getDailyFile();
    if (org.springframework.util.StringUtils.hasLength(dailyFile)) {
      String[] dailyFileIds = dailyFile.split(",");
      for (int z = 0; z < dailyFileIds.length; z++) {
        // 查询数据
        DailyfileEntity serviceEntity = (DailyfileEntity) dailyfileService.getEntity(Long.valueOf(dailyFileIds[z]));
        helper.addAttachment(new File(serviceEntity.getDailyfilePath()).getName(), new File(serviceEntity.getDailyfilePath()));
      }
    }
    // 发送信息
    jms.send(message);
  }


  @Override
  @Transactional(rollbackFor = Exception.class)
  public void dsfs() {
    List<DailyEntity> dailyEntityList = dailyDao.getDfs();
    for (DailyEntity daily : dailyEntityList) {
      DailyEntityVO vo = new DailyEntityVO();
      DailyHazardEntity dailyHazardEntity = new DailyHazardEntity();
      dailyHazardEntity.setDhDailyId(daily.getDailyId());
      List<DailyHazardEntity> listHa = dailyHazardService.query(dailyHazardEntity);


      DailyDetailEntity dailyDetailEntity = new DailyDetailEntity();
      dailyDetailEntity.setDdDailyId(daily.getDailyId());
      List<DailyDetailEntity> listDe = dailyDetailService.query(dailyDetailEntity);

      DailyPlanEntity dailyPlanEntity = new DailyPlanEntity();
      dailyPlanEntity.setDpDailyId(daily.getDailyId());
      List<DailyPlanEntity> listPl = dailyPlanService.query(dailyPlanEntity);
      vo.setDaily(daily);
      vo.setDailyHazard(listHa);
      vo.setDailyPlan(listPl);
      vo.setDailyDetail(listDe);
      vo.setSende(true);
      try {
        this.saveOrUpdateAndSend(vo, daily.getDayilUserId());
        System.out.println("成功");
      } catch (Exception e) {
        System.out.println("发送失败");
      }
    }
  }

  @Override
  public void rbwfs() {
    List<UserEntity> users = userService.querySendUsers();
    for (UserEntity user : users) {
      Integer daily = dailyDao.queryTodyByUserId(user.getUserId());
      if (daily == 0) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sendSmsService.sendSms(user.getUserTelephone(), sdf.format(date));
      }
    }
  }
}
