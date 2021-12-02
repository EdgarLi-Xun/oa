package com.sega.application.oa.controller.daily;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.entity.daily.DailyDetailEntity;
import com.sega.application.oa.entity.daily.DailyHazardEntity;
import com.sega.application.oa.service.daily.IDailyPlanService;
import com.sega.application.oa.entity.daily.DailyPlanEntity;
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
import com.sega.application.oa.controller.system.BaseController;
import com.alibaba.fastjson.JSONArray;

/**
 * 管理控制层
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:30:26<br/>
 * 历史修订：<br/>
 */
@RestController
@CrossOrigin
@RequestMapping("/apis/daily/dailyPlan")
public class DailyPlanController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DailyPlanController.class);
	/**
	 * 注入业务层
	 */	
	@Autowired
	private IDailyPlanService dailyPlanService;
	
	/**
     *新增或更新数据
     * @param dailyPlan
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public void saveOrUpdate(@RequestBody DailyPlanEntity dailyPlan, HttpServletResponse response, HttpServletRequest request) {
        try {
        	if (dailyPlan.getDpId() == null) {
                dailyPlan.setDpCreateDate(new Date());
                dailyPlan.setDpCreateBy(this.getUserBySession(request).getUserId());
        		dailyPlanService.saveEntity(dailyPlan);
                this.outJson(response, new OutData(true,"新增成功",dailyPlan), "yyyy-MM-dd");
        	}else{
                dailyPlanService.updateEntity(dailyPlan);
                dailyPlan.setDpUpdateBy(this.getUserBySession(request).getUserId());
                dailyPlan.setDpUpdateDate(new Date());
                this.outJson(response, new OutData(true,"更新成功",dailyPlan), "yyyy-MM-dd");
        	}
        }catch (Exception e){
            log.error(e.getMessage(),e);
            if (dailyPlan.getDpId() == null) {
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
			DailyPlanEntity dailyPlan = new DailyPlanEntity();
            dailyPlan.setDpId(tid);
            dailyPlan.setDpDel(1);
            dailyPlan.setDpUpdateDate(new Date());
            dailyPlanService.updateEntity(dailyPlan);
			this.outJson(response, new OutData(false,"删除成功",dailyPlan), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}

    /**
	 * 逻辑删除
	 * @param tids
	 * @return
	 */
	@PostMapping("/deleteByIds")
	@ResponseBody
	public void delete( @RequestBody  String tids, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
		try {
            List<Long> ids = JSONArray.parseArray(tids,Long.class);
            for(Long tid : ids){
                DailyPlanEntity dailyPlan = new DailyPlanEntity();
                dailyPlan.setDpId(tid);
                dailyPlan.setDpDel(1);
                dailyPlan.setDpUpdateDate(new Date());
                dailyPlanService.updateEntity(dailyPlan);
            }
			this.outJson(response, new OutData(false,"删除成功"), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}
	
	/**
     * 按条件分页查询数据
     * @param  dailyPlan
     * @return
     */
    @PostMapping("/queryByPage")
    @ResponseBody
    public void queryByPage(@RequestBody DailyPlanEntity dailyPlan, HttpServletResponse response, HttpServletRequest request) {
        try {

            if (dailyPlan.getPageNum() == null) {
                dailyPlan.setPageNum(0);
            }
            if (dailyPlan.getPageSize() == null) {
               dailyPlan.setPageSize(20);
            }
            PageHelper.startPage(dailyPlan.getPageNum(),dailyPlan.getPageSize());
            List<DailyPlanEntity> list = dailyPlanService.query(dailyPlan);
            this.outJson(response,  new OutData(true,"查询成功",new PageInfo<DailyPlanEntity>(list)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }

    /**
     * 根据日报id查询日报明日计划
     * @param dailyid
     * @return
     */
    @PostMapping("/querydailyplanBydailyid")
    @ResponseBody
    public void querydailyplanBydailyid( @RequestBody  String dailyid, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
        String daid=dailyid.replaceAll("=","");
        DailyPlanEntity dailyPlanEntity=new DailyPlanEntity();
        dailyPlanEntity.setDpDailyId(Long.parseLong(daid));
        List<DailyHazardEntity> list = dailyPlanService.query(dailyPlanEntity);
        this.outJson(response,  new OutData(true,"查询成功",list), "yyyy-MM-dd");
    }

    
    /**
     * 按条件查询数据
     * @param dailyPlan
     * @return
     */
    @PostMapping("/queryAll")
    @ResponseBody
    public void queryAll(@RequestBody DailyPlanEntity dailyPlan, HttpServletResponse response, HttpServletRequest request) {
        try {
            List<DailyPlanEntity> list = dailyPlanService.query(dailyPlan);
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
            this.outJson(response,  new OutData(true,"查询成功",dailyPlanService.getEntity(id)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }
}