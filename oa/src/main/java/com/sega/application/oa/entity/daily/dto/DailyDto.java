package com.sega.application.oa.entity.daily.dto;

import com.sega.application.oa.entity.daily.DailyEntity;
import lombok.Data;

/**
 * @description:
 * @author: EdgarLi
 * @date: 2021-12-01 17:18
 **/
@Data
public class DailyDto extends DailyEntity {
  private String sendTime;
}
