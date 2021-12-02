package com.sega.application.oa.controller.project;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.controller.system.BaseController;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.project.IProjectMemberService;
import com.sega.application.oa.entity.project.ProjectMemberEntity;
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
 * 创建日期：2019-10-24 17:02:21<br/>
 * 历史修订：<br/>
 */
@RestController
@CrossOrigin
@RequestMapping("/apis/projectMember")
public class ProjectMemberController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(ProjectMemberController.class);
	/**
	 * 注入业务层
	 */
	@Autowired
	private IProjectMemberService projectMemberService;

	@Autowired
    private IUserService userService;

	/**
     *新增或更新数据
     * @param projectMember
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public void saveOrUpdate(@RequestBody ProjectMemberEntity projectMember, HttpServletResponse response, HttpServletRequest request) {
        try {
            UserEntity user=(UserEntity) userService.getEntity(projectMember.getPmUserId());
            projectMember.setPmUserName(user.getUserName());
        	if (projectMember.getPmId() == null) {
                projectMember.setPmCreateDate(new Date());
                projectMember.setPmCreateBy(this.getUserBySession(request).getUserId());
        		projectMemberService.saveEntity(projectMember);
                this.outJson(response, new OutData(true,"新增成功",projectMember), "yyyy-MM-dd");
        	}else{
                projectMemberService.updateEntity(projectMember);
                projectMember.setPmUpdateBy(this.getUserBySession(request).getUserId());
                projectMember.setPmUpdateDate(new Date());
                this.outJson(response, new OutData(true,"更新成功",projectMember), "yyyy-MM-dd");
        	}
        }catch (Exception e){
            log.error(e.getMessage(),e);
            if (projectMember.getPmId() == null) {
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
			ProjectMemberEntity projectMember = new ProjectMemberEntity();
            projectMember.setPmId(tid);
            projectMember.setPmDel(1);
            projectMember.setPmUpdateDate(new Date());
            projectMemberService.updateEntity(projectMember);
			this.outJson(response, new OutData(false,"删除成功",projectMember), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}

    /**
     * 查询登录人下的所有项目
     */
    @PostMapping("/queryprojectList")
    @ResponseBody
    public void queryprojectList(@RequestBody  String username, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
        try {
            String newnusername=username.split("=")[0];

            List<Map<String,Object>> list = projectMemberService.queryprojectList(newnusername);
            this.outJson(response,  new OutData(true,"查询成功",list), "yyyy-MM-dd");
        }catch (RuntimeException e){
            this.outJson(response, new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }

    @PostMapping("/queryprojectplanList")
    @ResponseBody
    public void queryprojectplanList(@RequestBody  String username, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
        try {
            String newnusername=username.split("=")[0];

            List<Map<String,Object>> list = projectMemberService.queryplanprojectList(newnusername);
            this.outJson(response,  new OutData(true,"查询成功",list), "yyyy-MM-dd");
        }catch (RuntimeException e){
            this.outJson(response, new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }
    /**
	 * 逻辑删除
	 * @param tids
	 * @return
	 */
	@PostMapping("/deleteByIds")
	@ResponseBody
	public void delete(@RequestBody String tids , HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
		try {
            List<Long> ids = JSONArray.parseArray(tids,Long.class);
            for(Long tid : ids){
                ProjectMemberEntity projectMember = new ProjectMemberEntity();
                projectMember.setPmId(tid);
                projectMember.setPmDel(1);
                projectMember.setPmUpdateDate(new Date());
                projectMemberService.updateEntity(projectMember);
            }
			this.outJson(response, new OutData(false,"删除成功"), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}

	/**
     * 按条件分页查询数据
     * @param  projectMember
     * @return
     */
    @PostMapping("/queryByPage")
    @ResponseBody
    public void queryByPage(@RequestBody ProjectMemberEntity projectMember, HttpServletResponse response, HttpServletRequest request) {
        try {

            if (projectMember.getPageNum() == null) {
                projectMember.setPageNum(0);
            }
            if (projectMember.getPageSize() == null) {
               projectMember.setPageSize(20);
            }
            PageHelper.startPage(projectMember.getPageNum(),projectMember.getPageSize());
            List<ProjectMemberEntity> list = projectMemberService.query(projectMember);
            this.outJson(response,  new OutData(true,"查询成功",new PageInfo<ProjectMemberEntity>(list)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }

    /**
     * 按条件查询数据
     * @param projectMember
     * @return
     */
    @PostMapping("/queryAll")
    @ResponseBody
    public void queryAll(@RequestBody ProjectMemberEntity projectMember, HttpServletResponse response, HttpServletRequest request) {
        try {
            List<ProjectMemberEntity> list = projectMemberService.query(projectMember);
            this.outJson(response,  new OutData(true,"查询成功",list), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }

    /**
     * 根据ID获取数据
     *
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    @ResponseBody
    public void get(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) {
    	try {
            this.outJson(response,  new OutData(true,"查询成功",projectMemberService.getEntity(id)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }
}
