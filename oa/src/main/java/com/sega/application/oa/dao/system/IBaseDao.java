package com.sega.application.oa.dao.system;

import com.sega.application.oa.entity.system.BaseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IBaseDao {

	/**
	 * 根据ID查询实体信息
	 * @param id 实体ID
	 * @return 返回实体
	 */
	BaseEntity getById(Long id);

	/**
	 * 根据实体获取实体
	 * @param baseEntity 实体
	 * @return 返回实体
	 */
	BaseEntity getEntity(BaseEntity baseEntity);

	/**
	 * 查询所有
	 * @return 实体集合
	 */
	List queryAll();

	/**
	 * 根据实体查询实体集合
	 * @param baseEntity 实体
	 * @return 实体集合
	 */
	List query(BaseEntity baseEntity);

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
	void saveBatch(List list);

	/**
	 * 更新实体
	 * @param baseEntity 实体
	 */
	void updateEntity(BaseEntity baseEntity);

	/**
	 * 根据ids删除实体
	 * @param ids
	 */
	void deleteByIds(@Param("ids") String[] idArray);

	/**
	 * 根据id删除实体
	 * @param id
	 */
	void deleteById(Long id);

	/**
	 * 根据实体删除实体
	 * @param entity
	 */
	void deleteEntity(BaseEntity baseEntity);

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
