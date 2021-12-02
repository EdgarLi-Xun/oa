package com.sega.application.oa.service.humanengineering;

import com.sega.application.oa.entity.humanEngineering.AttendanceRecordEntity;
import java.util.List;
import java.util.Map;

import com.sega.application.oa.service.system.IBaseService;

/**
 * 业务层
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2020-1-8 15:57:37<br/>
 * 历史修订：<br/>
 */
public interface IAttendanceRecordService extends IBaseService {
    void  insert(AttendanceRecordEntity ar);

    void importExcel(String[] path);
}