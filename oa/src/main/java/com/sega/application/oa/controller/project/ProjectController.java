package com.sega.application.oa.controller.project;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.controller.system.BaseController;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.project.IProjectMemberService;
import com.sega.application.oa.service.project.IProjectService;
import com.sega.application.oa.entity.project.ProjectEntity;
import com.sega.application.oa.service.system.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sega.application.oa.entity.system.OutData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Date;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 管理控制层
 * @author 孙乾
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2019-10-22 15:02:24<br/>
 * 历史修订：<br/>
 */
@RestController
@CrossOrigin
@RequestMapping("/apis/project")
public class ProjectController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
	/**
	 * 注入业务层
	 */
	@Autowired
	private IProjectService projectService;

	@Autowired
  private IUserService userService;

	@Autowired
  private IProjectMemberService projectMemberService;

	/**
     *新增或更新数据
     * @param project
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public void saveOrUpdate(@RequestBody ProjectEntity project, HttpServletResponse response, HttpServletRequest request) {
        try {
            UserEntity user=(UserEntity) userService.getEntity(project.getProjectManagerId());
            project.setPrjectManagerName(user.getUserName());

            if (project.getProjectId() == null) {
                  project.setProjectCreateDate(new Date());
                  project.setProjectCreateBy(this.getUserBySession(request).getUserId());
                  projectService.save(project);
                  this.outJson(response, new OutData(true,"新增成功",project), "yyyy-MM-dd");
            }else{
                  project.setProjectUpdateBy(this.getUserBySession(request).getUserId());
                  project.setProjectUpdateDate(new Date());

                  projectService.update(project);
                  this.outJson(response, new OutData(true,"更新成功",project), "yyyy-MM-dd");
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            if (project.getProjectId() == null) {
                this.outJson(response, new OutData(false,"新增失败"), "yyyy-MM-dd");
            }else{
                this.outJson(response, new OutData(false,"更新失败"), "yyyy-MM-dd");
        	}
        }
    }

    /**
	 * 逻辑删除
	 * @param tid
	 * @return
	 */
	@DeleteMapping("/delete/{tid}")
	@ResponseBody
	public void delete(@PathVariable Long tid, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
		try {
			ProjectEntity project = new ProjectEntity();
            project.setProjectId(tid);
            project.setProjectDel(1);
            project.setProjectUpdateDate(new Date());
            projectService.updateEntity(project);
			this.outJson(response, new OutData(false,"删除成功",project), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}

	/**
     * 按条件分页查询数据
     * @param  project
     * @return
     */
    @PostMapping("/queryByPage")
    @ResponseBody
    public void queryByPage(@RequestBody ProjectEntity project, HttpServletResponse response, HttpServletRequest request) {
        try {
            if(project.getPageNum() == null){
              project.setPageNum(0);
            }
            if(project.getPageSize() == null){
              project.setPageSize(0);
            }
            PageHelper.startPage(project.getPageNum(),project.getPageSize());
            List<ProjectEntity> list = projectService.queryByPage(project);
            this.outJson(response,  new OutData(true,"查询成功",new PageInfo<ProjectEntity>(list)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }

    /**
     * 按条件查询数据
     * @param project
     * @return
     */
    @PostMapping("/queryAll")
    @ResponseBody
    public void queryAll(@RequestBody ProjectEntity project, HttpServletResponse response, HttpServletRequest request) {
        try {
            List<ProjectEntity> list = projectService.query(project);
            this.outJson(response,  new OutData(true,"查询成功",list), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }

  /**
   * 按条件查询数据
   * @param user
   * @return
   */
  @PostMapping("/queryUserAll")
  @ResponseBody
  public void queryUserAll(@RequestBody UserEntity user, HttpServletResponse response, HttpServletRequest request) {
    try {
      List<UserEntity> list = userService.query(user);
      this.outJson(response,  new OutData(true,"查询成功",list), "yyyy-MM-dd");
    } catch (RuntimeException e){
      log.error(e.getMessage(),e);
      this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
    }
  }

    /**
     * 根据项目查询用户信息
     * @param project
     * @return
     */
    @PostMapping("/queryuserByprojectId")
    @ResponseBody
    public void queryuserByprojectId(@RequestBody ProjectEntity project, HttpServletResponse response, HttpServletRequest request) {
        try {
            List<Map<String,Object>> list = projectService.queryuserByprojectId(project);
            this.outJson(response,  new OutData(true,"查询成功",list), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }


    /**
     * 逻辑删除
     * @param tids
     * @return
     */
    @PostMapping("/deleteByIds")
    @ResponseBody
    public void delete(@RequestBody  String tids, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
        try {
            List<Long> ids = JSONArray.parseArray(tids,Long.class);
            for(Long tid : ids){
                ProjectEntity projectEntity = new ProjectEntity();
                projectEntity.setProjectId(tid);
                projectEntity.setProjectDel(1);
                projectEntity.setProjectUpdateDate(new Date());
                projectService.updateEntity(projectEntity);
                projectMemberService.deleteByProjectId(projectEntity.getProjectId());
            }
            this.outJson(response, new OutData(false,"删除成功"), "yyyy-MM-dd");
        }catch (RuntimeException e){
            this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
        }
    }
    /**
     * 根据ID获取数据
     * @param id
     *
     * @return
     */
    @GetMapping("/get/{id}")
    @ResponseBody
    public void get(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) {
    	try {
            this.outJson(response,  new OutData(true,"查询成功",projectService.getEntity(id)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }
}
