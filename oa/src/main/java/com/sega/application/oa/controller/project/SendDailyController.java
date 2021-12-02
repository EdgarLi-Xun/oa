package com.sega.application.oa.controller.project;

import com.sega.application.oa.controller.system.BaseController;
import com.sega.application.oa.entity.system.MailBox;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.project.IDailyService;
import com.sega.application.oa.utils.HolidayUtil;
import com.sega.application.oa.utils.ReportFormUtil;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日报统计
 */

@RestController
@CrossOrigin
public class SendDailyController extends BaseController {
  private static final Logger log = LoggerFactory.getLogger(TaskTimeController.class);

  private static HashMap<String, List<String>> map = new HashMap<>();

  /**
   * 注入业务层
   */

  @Autowired
  private IDailyService dailyService;

  /**
   * 统计一天内 没有发日报的人员信息
   * 第二天的早上5点59分59秒发送邮件
   */


  /**
   * 每月1号早上8点，发送上个月未发日报人员统计
   */

  @Scheduled(cron = "59 59 7 1 * ?")
  public void dailyMonthStatistics() throws Exception {
    UserEntity entity = new UserEntity();
    Properties pros = new Properties();
    // 读取数据库中发送人的邮箱账号，如果没有从配置文件里拿默认的邮箱和密码。
    String fsr = dailyService.getFsr(0);
    String password = dailyService.getPassword(fsr);
    if (fsr == null || "".equals(fsr)) {
      try {
        pros.load(new InputStreamReader(PropertiesUtil.class.getResourceAsStream("/application-dev.yml"), "UTF-8"));
        fsr = pros.getProperty("accountNumber");
        password = pros.getProperty("password");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    entity.setUserEmail(fsr);
    entity.setEmailPassword(password);
    // 时间到了之后把map集合给发送出去即可
    String title = "每月漏发日报人员统计";
    // 收件人设置为人事部人员，从数据库查出 ;
    MailBox mailBox = dailyService.getJsr(2);

    if (mailBox.getJsz() != null && mailBox.getJsz() != 0) {

      List<String> sjrs = dailyService.getEmail(mailBox.getJsz());
      // 接收人
      if (mailBox.getJsr() != null) {
        if (map.size() == 0) {
          // 发送一句这个月没有人漏发日报
          ReportFormUtil.nobodyMonthFrom(entity, title, mailBox.getJsr());
        } else {
          // 发送没有发日报人员名单
          ReportFormUtil.dayilMonthFrom(map, entity, title, mailBox.getJsr());
        }
      }
      // 接受组
      if (sjrs.size() != 0) {
        for (String sjr : sjrs) {
          if (map.size() == 0) {
            // 发送一句这个月没有人漏发日报
            ReportFormUtil.nobodyMonthFrom(entity, title, mailBox.getJsr());
          } else {
            // 发送没有发日报人员名单
            ReportFormUtil.dayilMonthFrom(map, entity, title, sjr);
          }
        }
      }
    }

    if (mailBox.getCsz() != null && mailBox.getCsz() != 0) {
      List<String> cszs = dailyService.getEmail(mailBox.getCsz());

      // 抄送人
      if (mailBox.getCsr() != null) {
        if (map.size() == 0) {
          // 发送一句这个月没有人漏发日报
          ReportFormUtil.nobodyMonthFrom(entity, title, mailBox.getJsr());
        } else {
          // 发送没有发日报人员名单
          ReportFormUtil.dayilMonthFrom(map, entity, title, mailBox.getCsr());
        }
      }

      // 抄送组
      if (cszs.size() != 0) {
        for (String csr : cszs) {
          if (map.size() == 0) {
            // 发送一句今日没有人漏发日报
            ReportFormUtil.nobodyFrom(entity, title, csr);
          } else {
            // 发送没有发日报人员名单
            ReportFormUtil.dayilMonthFrom(map, entity, title, csr);
          }
        }
      }
    }

    List<String> sjrs = dailyService.getEmail(mailBox.getJsz());
    for (String sjr : sjrs) {
      ReportFormUtil.dayilMonthFrom(map, entity, title, sjr);
    }
    // 每个月发送完邮件，需要将map集合里的数据清空
    for (String key : map.keySet()) {
      List<String> list = map.get(key);
      for (int i = 0; i < list.size(); i++) {
        list.remove(i);
      }
      map.remove(key);
    }
  }

}
