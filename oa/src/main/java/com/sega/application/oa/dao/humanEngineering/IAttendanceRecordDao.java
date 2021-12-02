package com.sega.application.oa.dao.humanEngineering;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.sega.application.oa.entity.humanEngineering.AttendanceRecordEntity;
import java.util.List;
import java.util.Map;

import com.sega.application.oa.dao.system.IBaseDao;

/**
 * 持久层
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2020-1-8 15:57:37<br/>
 * 历史修订：<br/>
 */
@Mapper
public interface IAttendanceRecordDao extends IBaseDao {
    void insert(AttendanceRecordEntity ar);
}