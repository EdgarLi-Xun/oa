package com.sega.application.oa.service.system.impl;

import com.alibaba.fastjson.JSONObject;
import com.sega.application.oa.base.RedisClient;
import com.sega.application.oa.dao.system.*;
import com.sega.application.oa.entity.system.*;
import com.sega.application.oa.service.system.IUserService;
import com.sega.application.oa.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理持久化层
 * @author 邱小兵
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2017-8-1 21:16:48<br/>
 * 历史修订：<br/>
 */
 @Service("userBizImpl")
public class UserServiceImpl extends BaseServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;

	@Autowired
	private IRoleDao roleDao;

	@Autowired
	private IRoleModelDao roleModelDao;

	@Autowired
	private IDepartmentDao departmentDao;

	@Autowired
	private RedisClient redis;

  @Value("${sega.password}")
  private String password;
	@Override
	protected IBaseDao getDao() {
		return userDao;
	}

	@Override
	public UserEntity getByAccount(String account) {
		return userDao.getByAccount(account);
	}

	/**
	 * 批量导入用户
	 * @param list
	 * @return
	 */
	@Override
	public Boolean saveList(List<List<String>> list){
		try {

			for(List<String> _list : list){
				RoleEntity role = new RoleEntity();
				DepartmentEntity department = new DepartmentEntity();
				UserEntity user = new UserEntity();
				//判断该记录是否存在,用户名不存在表示该用户不存在
				if(StringUtils.isBlank(_list.get(0))){
					continue;
				}
				user.setUserLoginName(_list.get(0));
				if(StringUtils.isNotBlank(_list.get(1))){
					user.setUserPassword(StringUtil.MD5(_list.get(1)));
				}
				user.setUserName(_list.get(2));
				//角色,该角色必须在角色列表中存在
				role.setRoleName(_list.get(3));
				role = (RoleEntity) roleDao.getEntity(role);
				if(role != null){
					user.setUserRoleId(role.getRoleId());
				}

				if(_list.get(4).equals("男")){
					user.setUserGender(0);
				}else if(_list.get(4).equals("女")){
					user.setUserGender(1);
				}else{
					user.setUserGender(2);
				}
				user.setUserAge(Integer.parseInt(_list.get(5)));
				user.setUserIdCard(_list.get(6));
				//部门
				department.setDepartmentCode(_list.get(9));
				department = (DepartmentEntity) departmentDao.getEntity(department);
				if(department != null){
					user.setUserDepartmentId(department.getDepartmentId() );
				}
				user.setUserAddress(_list.get(10));
				user.setUserTelephone(_list.get(11));
				user.setUserEmail(_list.get(12));
				userDao.saveEntity(user);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
	}

	/**
	 * 根据模块编码查询分配了该菜单权限的所有用户
	 * @param modelCode
	 * @return
	 */
	@Override
	public List queryByModelCode(String modelCode) {
		RoleModelEntity roleModel = new RoleModelEntity();
		ModelEntity model = new ModelEntity();
		model.setModelCode(modelCode);
		roleModel.setModel(model);
		List<RoleModelEntity> roleList = roleModelDao.query(roleModel);
		if (roleList.size() == 0) {
			return new ArrayList<UserEntity>(0);
		}

		String ids = "";
		for (RoleModelEntity _roleModel : roleList) {
			ids += _roleModel.getRmRoleId() + ",";
		}
		ids = ids.substring(0, ids.length() - 1);
		return userDao.queryByRoleIds(ids);
	}

  @Override
  public void changePassword(UserEntity userEntity) {
	  userEntity.setOriginPassword(StringUtil.MD5(userEntity.getOriginPassword()));
    UserEntity oldUser = userDao.queryByUsernameAndPassword(userEntity);
    if(oldUser == null){
      throw new RuntimeException("原密码验证失败！");
    }
    oldUser.setUserPassword(StringUtil.MD5(userEntity.getUserPassword()));
    userDao.updateEntity(oldUser);
  }

  /**
	 * 获取用户的权限情况
	 * 检测权限信息
	 * @return
	 */
	public Map<String,Object> getMenuAuthCode(HttpServletRequest request){
		try {
			Subject subject = SecurityUtils.getSubject();
			if (subject != null) {
				String token = (String) subject.getPrincipal();
				if (StringUtils.isNotBlank(token)) {
					String userJson = (String)redis.get(token);
					UserEntity user = JSONObject.parseObject(userJson,UserEntity.class);
					RoleModelEntity roleModelEntity = new RoleModelEntity();
					roleModelEntity.setRmRoleId(user.getUserRoleId());
					List<RoleModelEntity> list = roleModelDao.query(roleModelEntity);
					Map<String,Object> map = new HashMap<>();
					for(RoleModelEntity _roleModelEntity : list){
						ModelEntity modelEntity = _roleModelEntity.getModel();
						map.put(modelEntity.getModelCode(),modelEntity.getModelName());
					}
					return map;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

  @Override
  public void resetUserPass(UserEntity user) {
    user.setUserPassword(StringUtil.MD5(password));
    userDao.updateEntity(user);
  }

  @Override
  public Boolean checkByLoginName(UserEntity user) {
    return userDao.checkByLoginName(user.getUserLoginName(),user.getUserId())==0;
  }

  @Override
  public UserEntity getByUserId(Long userId) {
    return userDao.getByUserId(userId);
  }

  @Override
  public List queryAllUser() {
    return userDao.queryAllUser();
  }

    @Override
    public List<UserEntity> querySendUsers() {
        return userDao.querySendUsers();
    }

    @Override
  public List queryAll() {
    return userDao.queryAll();
  }
}
