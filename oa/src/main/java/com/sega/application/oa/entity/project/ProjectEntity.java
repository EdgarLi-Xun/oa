package com.sega.application.oa.entity.project;

import java.util.Date;
import java.util.List;

import com.sega.application.oa.entity.system.BaseEntity;
import lombok.Data;


/**
 * 实体
 * @author 孙乾
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2019-10-21 19:51:08<br/>
 * 历史修订：<br/>
 */
@Data
public class ProjectEntity extends BaseEntity {

	/**
	 * 项目id
	 */
	private Long projectId;

	/**
	 * 项目名称
	 */
	private String projectName;

	/**
	 * 项目管理Id
	 */
	private Long projectManagerId;

	/**
	 * 项目管理名称
	 */
	private String prjectManagerName;

	/**
	 *
	 */
	private String projectCustomer;

	/**
	 *
	 */
	private Double projectBudget;

	/**
	 * 备注
	 */
	private String projectRemark;

	/**
	 * 0：未启动，1:进行中，2：已结束
	 */
	private Integer projectState;

	/**
	 * 项目创建人
	 */
	private Long projectCreateBy;

	/**
	 * 项目创建时间
	 */
	private Date projectCreateDate;

	/**
	 * 项目更新人
	 */
	private Long projectUpdateBy;

	/**
	 * 项目更新时间
	 */
	private Date projectUpdateDate;

	/**
	 * 是否删除：0：未删除，1：已删除
	 */
	private Integer projectDel;

  /**
   * 项目上线日期
   */
  private Date approvalDate;

  /**
   * 项目上线日期
   */
  private Date launchDate;

  /**
   * 项目验收日期
   */
  private Date acceptanceDate;

  /**
   * pm项目ID
   */
  private long pmProjectId;

  /**
   *  项目成员
   */
  private String projectMemberStr;

  /**
   *  项目成员
   */
  private Long[] projectMembers;

  /**
   *  项目成员
   */
  private String projectMemberNames;
}


