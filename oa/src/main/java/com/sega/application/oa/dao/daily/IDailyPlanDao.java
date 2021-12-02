package com.sega.application.oa.dao.daily;

import org.apache.ibatis.annotations.Mapper;
import com.sega.application.oa.dao.system.IBaseDao;
import org.apache.ibatis.annotations.Param;

/**
 * 持久层
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:25:28<br/>
 * 历史修订：<br/>
 */
@Mapper
public interface IDailyPlanDao extends IBaseDao {

    void deleteplanBydailyid(@Param("dailyid") Long dailyid);
}