package com.sega.application.oa.service.system;

import java.util.List;

/**
 * 业务
 * @author 邱小兵
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2017-8-1 21:09:59<br/>
 * 历史修订：<br/>
 */
public interface IRoleModelService extends IBaseService {

	/**
	 * 根据文件导入数据批量保存
	 * @param list
	 */
	Integer saveByImportList(List<List<String>> list);
}