package com.sega.application.oa.controller.project;

import com.sega.application.oa.controller.system.BaseController;
import com.sega.application.oa.entity.daily.dto.TjTaskDto;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.project.IStatisticsService;
import com.sega.application.oa.utils.ReportFormUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 定时任务，发送邮件
 */
@RestController
@CrossOrigin
public class StatisticsController extends BaseController {
  private static final Logger log = LoggerFactory.getLogger(TaskTimeController.class);

  /**
   * 注入业务层
   */
  @Autowired
  private IStatisticsService statisticsService;


  /**
   * 统计一周内 项目和员工工作时长
   * 每周的周日 23点59分59秒发送邮件
   */

  @Scheduled(cron = "59 59 23 ? * SUN")
  public void statisticsDuration() {
    UserEntity entity = new UserEntity();
    entity.setUserEmail("zhangshangao@segasoft.cn");
    entity.setEmailPassword("Zsg7722810x");

    try {
      Calendar instance = Calendar.getInstance();
      // 因为定时器的原因，所以每周都会执行一次，执行时可以拿到当前时间
      Date date = new Date();
      // 一周以前的时间
      instance.setTime(new Date());
      instance.add(Calendar.DATE, -7);
      Date beforeTime = instance.getTime();
      // 把时间类型换成sqlDate；
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      // 当前时间
      String end = dateFormat.format(date);
      // 一周以前时间
      String start = dateFormat.format(beforeTime);
      java.sql.Date nowDate = new java.sql.Date(dateFormat.parse(end).getTime());
      java.sql.Date beforeDate = new java.sql.Date(dateFormat.parse(start).getTime());
      // 第一步需要查到所有的项目名称，员工名称集合
      List<TjTaskDto> objList = statisticsService.getObjectId();

      // 加条件，按部门查询 10~20 发送总经办 20~30 发送研发部 30~40 发送项目部
      // 总经办
      List<TjTaskDto> gmoList = statisticsService.getUserId(10);
      // 研发部
      List<TjTaskDto> rddList = statisticsService.getUserId(20);
      // 项目部
      List<TjTaskDto> pdList = statisticsService.getUserId(30);

      // 拿到时间和集合以后可以去数据库按条件查到项目一周的工时和员工一周的工时统计。
      // 统计项目一周的工时
      List<TjTaskDto> xmList = statisticsService.getTjxmzTaskTime(beforeDate, nowDate, objList);
      // 生成一个报表文件，将项目一周的工时统计进报表,并通过qq邮箱发送。
      String title = "项目一周工时统计";
      // 查询数据库获取收件人信息
      String sjr1 = statisticsService.getSjr(10);
      String sjr2 = statisticsService.getSjr(20);
      String sjr3 = statisticsService.getSjr(30);
      ReportFormUtil.projectForm(xmList, entity, title, sjr1);
      // 统计总经办员工一周的工时
      List<TjTaskDto> zjbList = statisticsService.getTjygzTaskTime(beforeDate, nowDate, gmoList);
      // 统计研发部员工一周的工时
      List<TjTaskDto> yfbList = statisticsService.getTjygzTaskTime(beforeDate, nowDate, rddList);
      // 统计项目部员工一周的工时
      List<TjTaskDto> xmbList = statisticsService.getTjygzTaskTime(beforeDate, nowDate, pdList);
      // 生成一个报表文件，将员工一周的工时统计进报表,并通过qq邮箱发送。
      String title1 = "员工一周工时统计";

      if (zjbList.size() != 0) {
        // 总经办的统计结果发送给 alan
        ReportFormUtil.userForm(zjbList, entity, title1, sjr1);
      }

      if (yfbList.size() != 0) {
        // 研发部的统计结果发送给 范中耀
        ReportFormUtil.userForm(yfbList, entity, title1, sjr2);
      }

      if (xmbList.size() != 0) {
        // 项目部的统计结果发送给 俞先春
        ReportFormUtil.userForm(xmbList, entity, title1, sjr3);
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
    }
  }

  @Scheduled(cron = "59 59 23 28-31 * ?")
  public void statisticalMonth() {
    final Calendar c = Calendar.getInstance();
    if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
      //是最后一天
      UserEntity entity = new UserEntity();
      entity.setUserEmail("zhangshangao@segasoft.cn");
      entity.setEmailPassword("Zsg7722810x");

      try {
        Calendar instance = Calendar.getInstance();
        // 因为定时器的原因，所以每月都会执行一次，执行时可以拿到当前时间
        Date date = new Date();
        // 一月以前的时间
        // 需判断当前月有多少天。
        int total = dayByMonth(date);
        instance.setTime(new Date());
        instance.add(Calendar.DATE, -total);
        Date beforeTime = instance.getTime();
        // 把时间类型换成sqlDate；
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 当前时间
        String end = dateFormat.format(date);
        // 一月以前时间
        String start = dateFormat.format(beforeTime);
        java.sql.Date nowDate = new java.sql.Date(dateFormat.parse(end).getTime());
        java.sql.Date beforeDate = new java.sql.Date(dateFormat.parse(start).getTime());
        // 第一步需要查到所有的项目名称，员工名称集合
        List<TjTaskDto> objList = statisticsService.getObjectId();
        // 加条件，按部门查询 10~20 发送总经办 20~30 发送研发部 30~40 发送项目部
        // 总经办
        List<TjTaskDto> gmoList = statisticsService.getUserId(10);
        // 研发部
        List<TjTaskDto> rddList = statisticsService.getUserId(20);
        // 项目部
        List<TjTaskDto> pdList = statisticsService.getUserId(30);
        // 拿到时间和集合以后可以去数据库按条件查到项目一周的工时和员工一周的工时统计。
        // 统计项目一月的工时
        List<TjTaskDto> xmList = statisticsService.getTjxmzTaskTime(beforeDate, nowDate, objList);
        // 生成一个报表文件，将项目一月的工时统计进报表,并通过qq邮箱发送。
        String title = "项目一月工时统计";
        // 查询数据库获取收件人信息
        String sjr1 = statisticsService.getSjr(10);

        String sjr2 = statisticsService.getSjr(20);

        String sjr3 = statisticsService.getSjr(30);

        ReportFormUtil.projectForm(xmList, entity, title, sjr1);

        // 统计总经办员工一周的工时
        List<TjTaskDto> zjbList = statisticsService.getTjygzTaskTime(beforeDate, nowDate, gmoList);
        // 统计研发部员工一周的工时
        List<TjTaskDto> yfbList = statisticsService.getTjygzTaskTime(beforeDate, nowDate, rddList);
        // 统计项目部员工一周的工时
        List<TjTaskDto> xmbList = statisticsService.getTjygzTaskTime(beforeDate, nowDate, pdList);
        // 生成一个报表文件，将员工一月的工时统计进报表,并通过qq邮箱发送。
        String title1 = "员工一月工时统计";

        if (zjbList.size() != 0) {
          // 总经办的统计结果发送给 alan
          ReportFormUtil.userForm(zjbList, entity, title1, sjr1);
        }

        if (yfbList.size() != 0) {
          // 研发部的统计结果发送给 范中耀
          ReportFormUtil.userForm(yfbList, entity, title1, sjr2);
        }

        if (xmbList.size() != 0) {
          // 项目部的统计结果发送给 俞先春
          ReportFormUtil.userForm(xmbList, entity, title1, sjr3);
        }

      } catch (Exception e) {
        log.error(e.getMessage(), e);
        e.printStackTrace();
      }
    }
  }

  /**
   * 根据日期判断本月有多少天
   */
  public static int dayByMonth(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;

    switch (month) {
      case 1:
      case 3:
      case 5:
      case 7:
      case 8:
      case 10:
      case 12:
        return 31;
      case 4:
      case 6:
      case 9:
      case 11:
        return 30;
      //对于2月份需要判断是否为闰年
      case 2:
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
          return 29;
        } else {
          return 28;
        }
      default:
        return 0;
    }
  }

}
