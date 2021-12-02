package com.sega.application.oa.service.system;

import com.sega.application.oa.entity.system.BaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IBaseService<T> {

	/**
	 * 根据ID查询实体信息
	 * @param id 实体ID
	 * @return 返回实体
	 */
  T getEntity(Long id);

	/**
	 * 根据实体获取实体
	 * @param baseEntity 实体
	 * @return 返回实体
	 */
  T getEntity(BaseEntity baseEntity);

	/**
	 * 查询所有
	 * @return 实体集合
	 */
	List<T> queryAll();

	/**
	 * 根据实体查询实体集合
	 * @param baseEntity 实体
	 * @return 实体集合
	 */
	List<T> query(BaseEntity baseEntity);

	/**
	 * 保存实体
	 * @param baseEntity 需保存实体
	 * @return 实体主键值
	 */
	int saveEntity(BaseEntity baseEntity);

	/**
	 * 批量保存
	 * @param list 实体集合
	 */
	void saveEntity(List list);

	/**
	 * 更新实体
	 * @param baseEntity 实体
	 */
	void updateEntity(BaseEntity baseEntity);

	/**
	 * 保存或更新
	 * 使用该方法时需要配置实体，否则无效
	 * @param baseEntity
	 */
	void saveOrUpdate(BaseEntity baseEntity);

	/**
	 * 根据ids删除实体
	 * @param ids
	 */
	void deleteEntity(String[] ids);

	/**
	 * 根据id删除实体
	 * 底层未实现此Dao
	 * @param id
	 */
	void deleteEntity(Long id);

	/**
	 * 根据实体删除实体
	 * @param entity
	 */
	void deleteEntity(BaseEntity entity);

	/**
	 * 忽略伪删除，强制删除数据
	 * @param id
	 */
	void forceDelete(Long id);

	/**
	 * 导入执行SQL
	 * @param sql
	 * @return
	 */
	Object excuteSql(@Param("sql") String sql);

	/**
	 * 执行查询SQL并且返回结果集
	 * @param sql
	 * @return
	 */
	List createSQLQuery(@Param("sql") String sql);
}
