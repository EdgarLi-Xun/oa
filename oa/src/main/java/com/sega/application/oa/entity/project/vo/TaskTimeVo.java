package com.sega.application.oa.entity.project.vo;

import com.sega.application.oa.entity.project.TaskTimeEntity;
import lombok.Data;

/**
 * @description: 日报查询
 * @author: EdgarLi
 * @date: 2021-05-05 20:47
 **/
@Data
public class TaskTimeVo extends TaskTimeEntity {

  //部门code
  private String code;
}
