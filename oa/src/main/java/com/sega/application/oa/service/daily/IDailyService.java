package com.sega.application.oa.service.daily;

import com.sega.application.oa.entity.daily.DailyEntity;
import com.sega.application.oa.entity.daily.vo.DailyEntityVO;
import com.sega.application.oa.service.system.IBaseService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务层
 *
 * @author 孙乾
 * @version 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:25:28<br/>
 * 历史修订：<br/>
 */
public interface IDailyService extends IBaseService {

  List<Object> queryList(DailyEntityVO vo);

  /**
   * 保存或者修改 是否发送
   *
   * @param vo
   * @param userId
   */
  void saveOrUpdateAndSend(DailyEntityVO vo, Long userId) throws Exception;

  List<DailyEntity> queryXs(DailyEntity daily);


  void dsfs();

  void rbwfs();
}
