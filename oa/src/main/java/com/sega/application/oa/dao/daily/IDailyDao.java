package com.sega.application.oa.dao.daily;

import com.sega.application.oa.entity.daily.DailyEntity;
import com.sega.application.oa.entity.daily.vo.DailyEntityVO;
import org.apache.ibatis.annotations.Mapper;
import com.sega.application.oa.dao.system.IBaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 持久层
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:25:28<br/>
 * 历史修订：<br/>
 */
@Mapper
public interface IDailyDao extends IBaseDao {

  List<Object> queryList(@Param("vo") DailyEntityVO vo);

  List<DailyEntity> queryXs(@Param("vo") DailyEntity dailyEntity);

  List<DailyEntity> getDfs();

  Integer queryTodyByUserId(@Param("userId") Long userId);
}
