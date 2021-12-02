package com.sega.application.oa.dao;

import com.sega.application.oa.dao.system.IBaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 持久层
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2019-10-24 17:02:21<br/>
 * 历史修订：<br/>
 */
@Mapper
public interface IFixedAssetsDao extends IBaseDao {
    List<Map<String,Object>> queryStatistics();
}