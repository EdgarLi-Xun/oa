package com.sega.application.oa.service.daily;


import com.sega.application.oa.entity.daily.DailyfileEntity;
import com.sega.application.oa.service.system.IBaseService;

import java.util.List;

/**
 * 业务层
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2020-3-4 17:09:44<br/>
 * 历史修订：<br/>
 */
public interface IDailyfileService extends IBaseService {

  DailyfileEntity querydailyfile();

  void deleteById(Long id);


  /**
   * 根据日报ID 删除
   *
   * @param dailyId
   */
  void deleteByDailyid(Long dailyId);

  List<DailyfileEntity> getByDailyid(Long dailyId);
}
