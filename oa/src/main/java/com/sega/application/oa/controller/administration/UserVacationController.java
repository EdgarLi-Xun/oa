package com.sega.application.oa.controller.administration;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.controller.system.BaseController;
import com.sega.application.oa.service.administration.IUserVacationService;
import com.sega.application.oa.entity.administration.UserVacationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sega.application.oa.entity.system.OutData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Date;
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
@RequestMapping("/apis/userVacation")
public class UserVacationController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(UserVacationController.class);
	/**
	 * 注入业务层
	 */	
	@Autowired
	private IUserVacationService userVacationService;
	
	/**
     *新增或更新数据
     * @param userVacation
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public void saveOrUpdate(@RequestBody UserVacationEntity userVacation, HttpServletResponse response, HttpServletRequest request) {
        try {
        	if (userVacation.getUvId() == null) {
                userVacation.setUvCreateDate(new Date());
                userVacation.setUvCreateBy(this.getUserBySession(request).getUserId());
        		userVacationService.saveEntity(userVacation);
                this.outJson(response, new OutData(true,"新增成功",userVacation), "yyyy-MM-dd");
        	}else{
                userVacationService.updateEntity(userVacation);
                userVacation.setUvUpdateBy(this.getUserBySession(request).getUserId());
                userVacation.setUvUpdateDate(new Date());
                this.outJson(response, new OutData(true,"更新成功",userVacation), "yyyy-MM-dd");
        	}
        }catch (Exception e){
            log.error(e.getMessage(),e);
            if (userVacation.getUvId() == null) {
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
			UserVacationEntity userVacation = new UserVacationEntity();
            userVacation.setUvId(tid);
            userVacation.setUvDel(1);
            userVacation.setUvUpdateDate(new Date());
            userVacationService.updateEntity(userVacation);
			this.outJson(response, new OutData(false,"删除成功",userVacation), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}

    /**
	 * 逻辑删除
	 * @param tid
	 * @return
	 */
	@DeleteMapping("/deleteByIds")
	@ResponseBody
	public void delete( List<Long> tids, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
		try {
            for(Long tid : tids){
                UserVacationEntity userVacation = new UserVacationEntity();
                userVacation.setUvId(tid);
                userVacation.setUvDel(1);
                userVacation.setUvUpdateDate(new Date());
                userVacationService.updateEntity(userVacation);
            }
			this.outJson(response, new OutData(false,"删除成功"), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}
	
	/**
     * 按条件分页查询数据
     * @param  userVacation
     * @return
     */
    @PostMapping("/queryByPage")
    @ResponseBody
    public void queryByPage(@RequestBody UserVacationEntity userVacation, HttpServletResponse response, HttpServletRequest request) {
        try {

            if (userVacation.getPageNum() == null) {
                userVacation.setPageNum(0);
            }
            if (userVacation.getPageSize() == null) {
               userVacation.setPageSize(20);
            }
            PageHelper.startPage(userVacation.getPageNum(),userVacation.getPageSize());
            List<UserVacationEntity> list = userVacationService.query(userVacation);
            this.outJson(response,  new OutData(true,"查询成功",new PageInfo<UserVacationEntity>(list)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }
    
    /**
     * 按条件查询数据
     * @param userVacation
     * @return
     */
    @PostMapping("/queryAll")
    @ResponseBody
    public void queryAll(@RequestBody UserVacationEntity userVacation, HttpServletResponse response, HttpServletRequest request) {
        try {
            List<UserVacationEntity> list = userVacationService.query(userVacation);
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
            this.outJson(response,  new OutData(true,"查询成功",userVacationService.getEntity(id)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }
}