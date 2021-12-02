package com.sega.application.oa.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.sega.application.oa.annotation.Module;
import com.sega.application.oa.base.JwtToken;
import com.sega.application.oa.base.RedisClient;
import com.sega.application.oa.constant.DataTypeEnum;
import com.sega.application.oa.constant.RedisConstant;
import com.sega.application.oa.constant.SessionConstant;
import com.sega.application.oa.entity.system.DepartmentEntity;
import com.sega.application.oa.entity.system.OutData;
import com.sega.application.oa.entity.system.RoleEntity;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.system.IDepartmentService;
import com.sega.application.oa.service.system.IUserService;
import com.sega.application.oa.utils.JwtUtil;
import com.sega.application.oa.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@Module("系统维护")
@CrossOrigin
public class LoginController extends BaseController {


  @Autowired
  private IUserService userService;

  @Autowired
  private IDepartmentService departmentService;

  @Autowired
  private RedisClient redis;


  private String allChildIds = "0";

  /**
   * 登出
   *
   * @param userEntity 登出
   * @param response
   * @param request
   */
  @RequestMapping("/logout")
  @ResponseBody
  public void logout(@RequestBody UserEntity userEntity, String checkCode, HttpServletResponse response, HttpServletRequest request) {
    SecurityUtils.getSubject().logout();
  }

  /**
   * 验证登录
   *
   * @param userEntity 前端提交的登录用户数据
   * @param response
   * @param request
   */
  @RequestMapping("/checkLogin")
  @ResponseBody
  public void checkLogin(@RequestBody UserEntity userEntity, String checkCode, HttpServletResponse response, HttpServletRequest request) {

//        String codeSession = (String) request.getSession().getAttribute("imageCode");
//
//            outJson(response, new OutData(false, "请输入验证码"));
//            return;
//        }
//        if(!codeSession.equalsIgnoreCase(checkCode)){
//            outJson(response, new OutData(false, "验证码错误"));
//            return;
//        }

    //根据账号从数据库中查找用户
    UserEntity _userEntity = (UserEntity) userService.getByAccount(userEntity.getUserLoginName());
    //从数据库查无此用户
    if (_userEntity == null) {
      outJson(response, new OutData(false, "用户不存在！"));
      return;
    }
    //MD5加密前端提交密码
    String password_web = StringUtil.MD5(userEntity.getUserPassword());
    //密码不匹配
    if (!_userEntity.getUserPassword().equals(password_web)) {
      outJson(response, new OutData(false, "用户名或密码错误！"));
      return;
    }
    String currentTimeMillis = String.valueOf(System.currentTimeMillis());
    String tokenStr = JwtUtil.sign(userEntity.getUserLoginName(), currentTimeMillis);
    JwtToken token = new JwtToken(tokenStr);
    //向shiro认证用户，提交过去的密码是前端的密码

    if (_userEntity.getUserDepartmentId() == null) {
      _userEntity.setDepartment(new DepartmentEntity());
    } else {
      //默认获取第一个
      _userEntity.setDepartment((DepartmentEntity) departmentService.getEntity(_userEntity.getUserDepartmentId()));
    }

    // 清除可能存在的shiro权限信息缓存
    if (redis.hasKey(RedisConstant.PREFIX_SHIRO_CACHE + userEntity.getUserLoginName())) {
      redis.del(RedisConstant.PREFIX_SHIRO_CACHE + userEntity.getUserLoginName());
    }

    redis.set(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + userEntity.getUserLoginName(), currentTimeMillis, Integer.parseInt(refreshTokenExpireTime));
    redis.set(tokenStr, JSONObject.toJSONString(_userEntity), Integer.parseInt(refreshTokenExpireTime));

    Subject subject = SecurityUtils.getSubject();
    subject.login(token);

    //加入用户当前所在公司
    DepartmentEntity company = new DepartmentEntity();
    if (_userEntity.getDepartment() != null && _userEntity.getDepartment().getCompany() != null) {
      company = _userEntity.getDepartment().getCompany();
    } else {
      company = _userEntity.getDepartment();
    }
    request.getSession().setAttribute(SessionConstant.COMPANY, company);

    request.getSession().setAttribute(SessionConstant.DEPARTMENT, _userEntity.getDepartment());

    //获取当前用户所属角色
    RoleEntity role = _userEntity.getRole();
    //数据权限查询语句
    String querySql = "";
    //批量倒入数据的查询语句
    String dataScorpDepartmentIds = _userEntity.getUserDepartmentId() + "";

    //设置到session中
    request.getSession().setAttribute(SessionConstant.DATASCORPSQL, querySql);

    //批量导入的数据权限
    if (DataTypeEnum.personal.getNumber() == role.getRoleDataType() || DataTypeEnum.department.getNumber() == role.getRoleDataType()) {
      //个人或部门级数据权限，只能当作是部门级数据权限
      dataScorpDepartmentIds = _userEntity.getUserDepartmentId() + "";
    } else if (DataTypeEnum.departmentAndSubordinate.getNumber() == role.getRoleDataType()) {
      //所在部门及以下数据权限
      dataScorpDepartmentIds += "," + queryChildDepartmentIds(_userEntity.getUserDepartmentId() + "", 0);
    } else if (DataTypeEnum.all.getNumber() == role.getRoleDataType()) {//权限为所在部门及以下数据
      //全部数据权限
      dataScorpDepartmentIds = "";
    }

    request.getSession().setAttribute(SessionConstant.DEPARTMENTDATASCORP, dataScorpDepartmentIds);

    JSONObject outObj = new JSONObject();

    outObj.put("token", tokenStr);
    outJson(response, new OutData(true, "成功", outObj));
  }

  /**
   * 递归查询所有子部门
   *
   * @param ids
   * @return
   */
  private String queryChildDepartmentIds(String ids, int type) {
    if (type == 0) {
      allChildIds = "0";
    }
    if (StringUtils.isBlank(ids)) {
      return allChildIds;
    }
    String curChilds = "";
    DepartmentEntity department = new DepartmentEntity();
    //设置父级部门
    department.setQuerySql(" and department_parent_id in(" + ids + ")");
    List<DepartmentEntity> departmentList = departmentService.query(department);
    for (DepartmentEntity _department : departmentList) {
      if (curChilds == "") {
        curChilds += _department.getDepartmentId();
      } else {
        curChilds += "," + _department.getDepartmentId();
      }
    }
    if (StringUtils.isBlank(allChildIds)) {
      allChildIds = curChilds;
    } else {
      if (StringUtils.isNotBlank(curChilds)) {
        allChildIds += "," + curChilds;
      }
    }
    return this.queryChildDepartmentIds(curChilds, 1);
  }

}
