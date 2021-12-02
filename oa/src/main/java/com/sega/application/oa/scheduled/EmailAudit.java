package com.sega.application.oa.scheduled;

import com.sega.application.oa.service.project.IDailyService;
import com.sega.application.oa.service.project.ITaskTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description: 工时审核
 * @author: EdgarLi
 * @date: 2021-02-04 16:17
 **/
@Configuration
@EnableScheduling
@Slf4j
public class EmailAudit {
  @Autowired
  private IDailyService dailyService;
  @Autowired
  private com.sega.application.oa.service.daily.IDailyService idailyService;


  @Autowired
  private ITaskTimeService taskTimeService;

  //  @Scheduled(cron = "0 */1 * * * ?")//每分钟查询一次
//  @Scheduled(cron = "59 59 23 1/1 * ? ")//每天23:59:59更新
  @Scheduled(cron = "59 59 5 * * ?")
  public void xqcfdwgjGenerate() {
    log.info("========================定时每天发送未发送日报人员=======================");
//    dailyService.dailyStatistics();
  }

  //  @Scheduled(cron = "0 */1 * * * ?")//每分钟查询一次
  @Scheduled(cron = "59 29 9 * * ?")//每天早上九点半
  public void gsfsgstjDay() {
    log.info("========================定时每天发送工时统计的=======================");
    taskTimeService.gsfsgstjDay();
  }

  @Scheduled(cron = "0 0 12 ? * 1")//每周一12点
//  @Scheduled(cron = "0 */1 * * * ?")//每分钟查询一次
  public void gsfsgstjWeek() {
    log.info("========================定时每周发送工时统计的=======================");
//    taskTimeService.gsfsgstjWeek();
  }

  @Scheduled(cron = "0 */1 17-23 * * ?")//每分钟查询一次
  public void dsfsrb() {
    idailyService.dsfs();
  }

  @Scheduled(cron = "59 59 20 1/1 * ?")//
  public void rbwfs() {
    idailyService.rbwfs();
  }

}
