package com.sega.application.oa.dao.daily;

import com.sega.application.oa.entity.daily.DailySenderEntity;
import org.apache.ibatis.annotations.Mapper;
import com.sega.application.oa.dao.system.IBaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 持久层
 * @author 孙乾
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:25:28<br/>
 * 历史修订：<br/>
 */
@Mapper
public interface IDailySenderDao extends IBaseDao {

    List<DailySenderEntity> querysendByPage(DailySenderEntity dailySenderEntity);

    List<Map<String, Object>> queryjsrEmail(@Param("username") String username);

    List<Map<String, Object>> querycsrEmail(@Param("username") String username);

    Map<String, Object> queryJsrAndCsrEmail(Long userId);
}
