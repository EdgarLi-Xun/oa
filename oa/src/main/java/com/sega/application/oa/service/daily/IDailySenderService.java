package com.sega.application.oa.service.daily;

import com.sega.application.oa.entity.daily.DailySenderEntity;
import com.sega.application.oa.service.system.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * 业务层
 * @author 孙乾
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:25:28<br/>
 * 历史修订：<br/>
 */
public interface IDailySenderService extends IBaseService {

    List<DailySenderEntity> querysendByPage(DailySenderEntity dailySenderEntity);

    List<Map<String, Object>> queryjsrEmail(String username);

    List<Map<String, Object>> querycsrEmail(String username);


  Map<String, Object> queryJsrAndCsrEmail(Long userId);
}
