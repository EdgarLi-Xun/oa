package com.sega.application.oa.service.system.impl;

import com.sega.application.oa.constant.SessionConstant;
import com.sega.application.oa.dao.system.IBaseDao;
import com.sega.application.oa.dao.system.IDepartmentDao;
import com.sega.application.oa.entity.system.DepartmentEntity;
import com.sega.application.oa.entity.system.RoleEntity;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.system.IDepartmentService;
import com.sega.application.oa.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理持久化层
 *
 * @author 邱小兵
 * @version 版本号：1.0.0<br/>
 * 创建日期：2017-8-1 21:16:47<br/>
 * 历史修订：<br/>
 */
@Service("departmentBizImpl")
public class DepartmentServiceImpl extends BaseServiceImpl implements IDepartmentService {


  @Autowired
  private IDepartmentDao departmentDao;


  @Override
  protected IBaseDao getDao() {
    return departmentDao;
  }

  /**
   * 部门树列表
   */
  private List<DepartmentEntity> departmentTreeList = new ArrayList<>();

  /**
   * 批量导入数据保存
   */
  @Override
  public Boolean saveList(List<List<String>> list) {
    try {
      for (List<String> _list : list) {
        DepartmentEntity _department = new DepartmentEntity();
        DepartmentEntity company = new DepartmentEntity();
        //设置部门编号
        _department.setDepartmentCode(_list.get(2));
        //查询当前实体是否已存在
        DepartmentEntity epartmentEntity = (DepartmentEntity) departmentDao.getEntity(_department);
        if (StringUtils.isNotBlank(_list.get(1))) {
          company.setDepartmentCode(_list.get(1));
          company = (DepartmentEntity) departmentDao.getEntity(company);
          if (company == null) {
            return false;
          }
        }
        if (epartmentEntity == null) {
          //部门类型
          _department.setDepartmentType(Integer.parseInt(_list.get(0)));
          //设置公司编号
          _department.setDepartmentCompanyId(company.getDepartmentId());
          //部门名称
          _department.setDepartmentName(_list.get(3));
          //部门编号
          _department.setDepartmentCode(_list.get(4));
          //上级部门ID
          if (StringUtils.isNotBlank(_list.get(5))) {
            DepartmentEntity paDepartmentEntity = new DepartmentEntity();
            paDepartmentEntity.setDepartmentCode(_list.get(5));
            paDepartmentEntity = (DepartmentEntity) departmentDao.getEntity(paDepartmentEntity);
            if (paDepartmentEntity != null) {
              _department.setDepartmentParentId(paDepartmentEntity.getDepartmentId());
            } else {
              _department.setDepartmentParentId(null);
            }
          } else {
            _department.setDepartmentParentId(null);
          }
          //部门地址
          _department.setDepartmentAddress(_list.get(6));
          //部门传真
          _department.setDepartmentFax(_list.get(7));
          //联系电话
          _department.setDepartmentTelephone(_list.get(8));
          //邮箱
          _department.setDepartmentEmail(_list.get(9));
          //介绍
          _department.setDepartmentIntroduction(_list.get(10));
          departmentDao.saveEntity(_department);
        }
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
      // TODO: handle exception
    }
  }

  @Override
  public List queryCompany(DepartmentEntity department) {
    return departmentDao.queryCompany(department);
  }

  @Override
  public List queryByCompany(DepartmentEntity department) {
    HttpServletRequest request = SpringUtil.getRequest();
    if (request == null) {
      return new ArrayList();
    }
    UserEntity user = (UserEntity) request.getSession().getAttribute(SessionConstant.USER);
    RoleEntity role = user.getRole();
    if (role.getRoleDataType() < 3) {
      String[] departmentIds = (user.getUserDepartmentId() + "").split(",");
      String companyIds = "0";
      for (String departmentId : departmentIds) {
        String companyId = "";
        DepartmentEntity _department = (DepartmentEntity) departmentDao.getById(Long.valueOf(departmentId));
        if (_department == null) {
          continue;
        }
        if (_department.getCompany() == null) {
          companyId = _department.getDepartmentId() + "";
        } else {
          companyId = _department.getCompany().getDepartmentId() + "";
        }
        companyIds += "," + companyId;
      }
      department.setQuerySql(" and (d.department_company_id in (" + companyIds + ") or d.department_id in (" + companyIds + ") )  ");
    }
    return departmentDao.query(department);
  }

  /**
   * 查询部门树状结构数据
   *
   * @return
   */
  public List<DepartmentEntity> queryTreeData() {
    List<DepartmentEntity> departmentEntities = departmentDao.queryAll();
    return this.buildByRecursive(departmentEntities);
  }

  @Override
  public boolean checkByDepartmentCode(DepartmentEntity department) {
    return departmentDao.checkByDepartmentCode(department.getDepartmentCode(),department.getDepartmentId()) == 0;
  }

  @Override
  public DepartmentEntity getEntityById(Long userDepartmentId) {
    return departmentDao.getEntityById(userDepartmentId);
  }

  /**
   * list转Tree
   *
   * @param departmentList
   * @return
   */
  public static List<DepartmentEntity> list2tree(List<DepartmentEntity> departmentList) {
    List<DepartmentEntity> trees = new ArrayList<DepartmentEntity>();
    for (DepartmentEntity parentDepartment : departmentList) {
      boolean isRoot = true;
      for (DepartmentEntity department : departmentList) {
        if (parentDepartment.getDepartmentParentId() != null && parentDepartment.getDepartmentParentId().equals(department.getDepartmentId())) {
          isRoot = false;
          if (department.getChildren() == null) {
            department.setChildren(new ArrayList<DepartmentEntity>());
          }
          department.getChildren().add(parentDepartment);
          break;
        }
      }
      if (isRoot) {
        trees.add(parentDepartment);
      }
    }
    return trees;
  }

  /**
   * 使用递归方法建树
   *
   * @param treeNodes
   * @return
   */
  public static List<DepartmentEntity> buildByRecursive(List<DepartmentEntity> treeNodes) {
    List<DepartmentEntity> trees = new ArrayList<DepartmentEntity>();
    for (DepartmentEntity treeNode : treeNodes) {
      if (treeNode.getDepartmentParentId() == null) {
        trees.add(findChildren(treeNode, treeNodes));
      }
    }
    return trees;
  }

  /**
   * 递归查找子节点
   *
   * @param treeNodes
   * @return
   */
  public static DepartmentEntity findChildren(DepartmentEntity treeNode, List<DepartmentEntity> treeNodes) {
    for (DepartmentEntity it : treeNodes) {
      if (treeNode.getDepartmentId().equals(it.getDepartmentParentId())) {
        if (treeNode.getChildren() == null) {
          treeNode.setChildren(new ArrayList<DepartmentEntity>());
        }
        treeNode.setExpand(true);
        treeNode.setDisabled(true);
        treeNode.getChildren().add(findChildren(it, treeNodes));
      }
    }
    return treeNode;
  }
}
