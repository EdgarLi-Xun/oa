package com.sega.application.oa.service.daily;

import com.sega.application.oa.service.system.IBaseService;

/**
 * 业务层
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:25:28<br/>
 * 历史修订：<br/>
 */
public interface IDailyDetailService extends IBaseService {
    void deletedetailBydailyid(Long dailyid);

}