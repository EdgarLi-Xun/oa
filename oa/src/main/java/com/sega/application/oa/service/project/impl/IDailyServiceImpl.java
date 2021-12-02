package com.sega.application.oa.service.project.impl;

import com.sega.application.oa.dao.ISendDailyDao;
import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.entity.daily.DailyEntity;
import com.sega.application.oa.entity.system.MailBox;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.project.IDailyService;
import com.sega.application.oa.service.system.IUserService;
import com.sega.application.oa.service.system.impl.BaseServiceImpl;
import com.sega.application.oa.utils.HolidayUtil;
import com.sega.application.oa.utils.ReportFormUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class IDailyServiceImpl extends BaseServiceImpl implements IDailyService {

  @Resource
  private ISendDailyDao dailyDao;

  @Resource
  private IUserService userService;

  @Value("${mail.accountNumber}")
  private String mailAccountNumber;

  @Value("${mail.password}")
  private String mailPassword;


  @Override
  protected IBaseDao getDao() {
    return null;
  }

  @Override
  public Set<String> getUserName(Date nowDate) {
    return dailyDao.getUserName(nowDate);
  }

  @Override
  public String getName(Long userId) {
    return dailyDao.getName(userId);
  }

  @Override
  public List<String> getEmail(Long jsz) {
    return dailyDao.getEmail(jsz);
  }

  @Override
  public String getFsr(Integer type) {
    return dailyDao.getFsr(type);
  }

  @Override
  public String getPassword(String fsr) {
    return dailyDao.getPassword(fsr);
  }

  @Override
  public MailBox getJsr(int jsr) {
    return dailyDao.getJsr(jsr);
  }

  @Override
  public void dailyStatistics() {


    // 从数据库读取收件人和收件组,抄送人和抄送组
    MailBox mailBox = this.getJsr(1);
    UserEntity adminUser = userService.getByUserId(mailBox.getFsr());

    Map<String, List<String>> map = new HashMap<>();
    UserEntity entity = new UserEntity();

    // 读取数据库中发送人的邮箱账号，如果没有从配置文件里拿默认的邮箱和密码。
    entity.setUserEmail(adminUser.getUserEmail());
    entity.setEmailPassword(adminUser.getEmailPassword());
    // 需要判断当天是否是休息日，（周末和放假日不发日报）
    // 因为定时器的原因，所以每天都会执行一次，执行时可以拿到当前时间
    // 把时间类型换成sqlDate；
    Calendar instance = Calendar.getInstance();
    // 一天以前的时间
    instance.setTime(new java.util.Date());

    instance.add(Calendar.DATE, -1);


    java.util.Date beforeDate = instance.getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
    String httpArg = f.format(beforeDate);
    String jsonResult = HolidayUtil.request(httpArg);

    // 当前时间
    String date = dateFormat.format(beforeDate);

    // 判断一天以前时间是否应该上班
    if ("0".equals(jsonResult)) {
      try {
        java.sql.Date nowDate = new java.sql.Date(dateFormat.parse(date).getTime());
        // 第一步需要查到所有的需要发日报的人员的id
        List<UserEntity> userList = userService.queryAllUser();
        // 没有发日报人员姓名集合
        ArrayList<String> userNameList = new ArrayList<>();
        Set<String> userName = this.getUserName(nowDate);

        for (UserEntity user : userList) {
          // 判断该员工有没有发日报

          if (!userName.contains(user.getUserName())) {
            userNameList.add(user.getUserName());

          }
        }
        String title = nowDate + "漏发日报人员统计";
        List<String> sjrs = new ArrayList<>();
        if (mailBox.getJsz() != null) {
          sjrs = this.getEmail(mailBox.getJsz());
        }
        String jsr = mailBox.getJsr();
        if (!StringUtils.isEmpty(jsr)) {
          sjrs.addAll(Arrays.asList(jsr.split(",")));
        }

        List<String> csrs = new ArrayList<>();
        if (mailBox.getCsz() != null) {
          csrs = this.getEmail(mailBox.getCsz());
        }
        String csr = mailBox.getCsr();
        if (!StringUtils.isEmpty(csr)) {
          csrs.addAll(Arrays.asList(csr.split(",")));
        }

        if (sjrs.size() > 0) {
          ReportFormUtil.dayilFrom(userNameList, entity, title, sjrs, csrs);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }


}
