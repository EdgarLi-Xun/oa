package com.sega.application.oa;

import cn.hutool.core.date.DateUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @description: 测试
 * @author: EdgarLi
 * @date: 2021-02-04 13:50
 **/
public class DateUtils {

  private static List<Date> getDates(int year, int month) {
    List<Date> dates = new ArrayList<Date>();

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.MONTH, month - 1);
    cal.set(Calendar.DATE, 1);


    while (cal.get(Calendar.YEAR) == year &&
      cal.get(Calendar.MONTH) < month) {
      int day = cal.get(Calendar.DAY_OF_WEEK);

      if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
        dates.add((Date) cal.getTime().clone());
      }
      cal.add(Calendar.DATE, 1);
    }
    return dates;

  }

  @Test
  public void t() {
    List<Date> dates = getDates(2021, 2);
    for (Date date : dates) {
      System.out.println(DateUtil.format(date, "yyyy-MM-dd"));
      System.out.println(DateUtil.dayOfWeek(date) - 1);
    }

  }
}
