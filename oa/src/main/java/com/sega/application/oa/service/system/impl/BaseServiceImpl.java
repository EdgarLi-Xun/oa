package com.sega.application.oa.service.system.impl;

import com.sega.application.oa.annotation.Id;
import com.sega.application.oa.annotation.Table;
import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.entity.system.BaseEntity;
import com.sega.application.oa.service.system.IBaseService;
import java.lang.reflect.Field;
import java.util.List;

public abstract class BaseServiceImpl implements IBaseService {

	/**
	 * 该方法由子类去实现
	 * @return 子类的实例Dao
	 */
	protected abstract IBaseDao getDao();

	/**
	 * 根据ID查询实体信息
	 * @param id 实体ID
	 * @return 返回实体
	 */
	public BaseEntity getEntity(Long id) {
		return getDao().getById(id);
	}

	/**
	 * 根据实体获取实体
	 * @param baseEntity 实体
	 * @return 返回实体
	 */
	public BaseEntity getEntity(BaseEntity baseEntity) {
		return getDao().getEntity(baseEntity);
	}

	/**
	 * 查询所有
	 * @return 实体集合
	 */
	public List queryAll() {
		return getDao().queryAll();
	}

	/**
	 * 根据实体查询实体集合
	 * @param baseEntity 实体
	 * @return 实体集合
	 */
	public List<BaseEntity> query(BaseEntity baseEntity) {
		return getDao().query(baseEntity);
	}

	/**
	 * 保存实体
	 * @param baseEntity 需保存实体
	 * @return 实体主键值
	 */
	public int saveEntity(BaseEntity baseEntity) {
		return getDao().saveEntity(baseEntity);
	}

	/**
	 * 批量保存
	 * @param list 实体集合
	 */
	public void saveEntity(List list) {
		if (list.size() > 0) {
			getDao().saveBatch(list);
		}
	}

	/**
	 * 更新实体
	 * @param baseEntity 实体
	 */
	public void updateEntity(BaseEntity baseEntity) {
		getDao().updateEntity(baseEntity);
	}
	
	/**
	 * 保存或更新
	 * @param baseEntity 实体
	 */
	public void saveOrUpdate(BaseEntity baseEntity) {
		Class entityClass = baseEntity.getClass();
		
		String table = ((Table) entityClass.getAnnotation(Table.class)).value();
		String idField = "";
		String idValue = "";
		
		Field[] fileds = entityClass.getDeclaredFields();
		for (Field field : fileds) {
			if (field.isAnnotationPresent(Id.class)) {
				idField = field.getAnnotation(Id.class).value();
				/*String idFieldName = field.getName();
				String idFieldMethod = "get" + StringUtil.upperCase(idFieldName);
				Method idMethod = entityClass.getMethod(idFieldMethod, String.class);
				idMethod.invoke(baseEntity, null);*/
				try {
					field.setAccessible(true);
					idValue = field.get(baseEntity).toString();
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				break;
			}
		}
		
		if (table != "" && idField != "" && idValue != "") {
			String sql = "select count(*) from " + table + " where " + idField + " = " + idValue;
			Integer count = Integer.parseInt(getDao().excuteSql(sql).toString());
			if (count == 0) {
				getDao().saveEntity(baseEntity);
			} else {
				getDao().updateEntity(baseEntity);
			}
		}
	}

	/**
	 * 根据ids删除实体
	 * @param idArray
	 */
	public void deleteEntity(String[] idArray) {
		if (idArray.length == 0) {
			return;
		}
		getDao().deleteByIds(idArray);
	}

	/**
	 * 根据id删除实体
	 * <b>底层并未实现deleteById的Dao<b>
	 * @param id
	 */
	public void deleteEntity(Long id) {
		getDao().deleteById(id);
	}

	/**
	 * 根据实体删除实体
	 * @param baseEntity
	 */
	public void deleteEntity(BaseEntity baseEntity) {
		getDao().deleteEntity(baseEntity);
	}

	/**
	 * 忽略伪删除，强制删除数据
	 */
	public void forceDelete(Long id) {
		getDao().forceDelete(id);
	}

	/**
	 * 导入执行SQL
	 * @param sql
	 */
	public Object excuteSql(String sql) {
		return getDao().excuteSql(sql);
	}
	
	/**
	 * 执行查询SQL并且返回结果集
	 * @param sql
	 */
	public List createSQLQuery(String sql) {
		return getDao().createSQLQuery(sql);
	}
}
