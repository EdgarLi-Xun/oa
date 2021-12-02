package com.sega.application.oa.controller.daily;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.service.daily.IDailyHazardService;
import com.sega.application.oa.entity.daily.DailyHazardEntity;
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
@RequestMapping("/apis/daily/dailyHazard")
public class DailyHazardController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DailyHazardController.class);
	/**
	 * 注入业务层
	 */	
	@Autowired
	private IDailyHazardService dailyHazardService;
	
	/**
     *新增或更新数据
     * @param dailyHazard
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public void saveOrUpdate(@RequestBody DailyHazardEntity dailyHazard, HttpServletResponse response, HttpServletRequest request) {
        try {
        	if (dailyHazard.getDhId() == null) {
                dailyHazard.setDhCreateDate(new Date());
                dailyHazard.setDhCreateBy(this.getUserBySession(request).getUserId());
        		dailyHazardService.saveEntity(dailyHazard);
                this.outJson(response, new OutData(true,"新增成功",dailyHazard), "yyyy-MM-dd");
        	}else{
                dailyHazardService.updateEntity(dailyHazard);
                dailyHazard.setDhUpdateBy(this.getUserBySession(request).getUserId());
                dailyHazard.setDhUpdateDate(new Date());
                this.outJson(response, new OutData(true,"更新成功",dailyHazard), "yyyy-MM-dd");
        	}
        }catch (Exception e){
            log.error(e.getMessage(),e);
            if (dailyHazard.getDhId() == null) {
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
			DailyHazardEntity dailyHazard = new DailyHazardEntity();
            dailyHazard.setDhId(tid);
            dailyHazard.setDhDel(1);
            dailyHazard.setDhUpdateDate(new Date());
            dailyHazardService.updateEntity(dailyHazard);
			this.outJson(response, new OutData(false,"删除成功",dailyHazard), "yyyy-MM-dd");
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
                DailyHazardEntity dailyHazard = new DailyHazardEntity();
                dailyHazard.setDhId(tid);
                dailyHazard.setDhDel(1);
                dailyHazard.setDhUpdateDate(new Date());
                dailyHazardService.updateEntity(dailyHazard);
            }
			this.outJson(response, new OutData(false,"删除成功"), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}

    /**
     * 根据日报id查询风险情况
     * @param dailyid
     * @return
     */
    @PostMapping("/querydailyhazardBydailyid")
    @ResponseBody
    public void querydailyhazardBydailyid( @RequestBody  String dailyid, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
        String daid=dailyid.replaceAll("=","");
        DailyHazardEntity dailyHazardEntity=new DailyHazardEntity();
        dailyHazardEntity.setDhDailyId(Long.parseLong(daid));
        List<DailyHazardEntity> list = dailyHazardService.query(dailyHazardEntity);
        this.outJson(response,  new OutData(true,"查询成功",list), "yyyy-MM-dd");
    }

	/**
     * 按条件分页查询数据
     * @param  dailyHazard
     * @return
     */
    @PostMapping("/queryByPage")
    @ResponseBody
    public void queryByPage(@RequestBody DailyHazardEntity dailyHazard, HttpServletResponse response, HttpServletRequest request) {
        try {

            if (dailyHazard.getPageNum() == null) {
                dailyHazard.setPageNum(0);
            }
            if (dailyHazard.getPageSize() == null) {
               dailyHazard.setPageSize(20);
            }
            PageHelper.startPage(dailyHazard.getPageNum(),dailyHazard.getPageSize());
            List<DailyHazardEntity> list = dailyHazardService.query(dailyHazard);
            this.outJson(response,  new OutData(true,"查询成功",new PageInfo<DailyHazardEntity>(list)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }
    
    /**
     * 按条件查询数据
     * @param dailyHazard
     * @return
     */
    @PostMapping("/queryAll")
    @ResponseBody
    public void queryAll(@RequestBody DailyHazardEntity dailyHazard, HttpServletResponse response, HttpServletRequest request) {
        try {
            List<DailyHazardEntity> list = dailyHazardService.query(dailyHazard);
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
            this.outJson(response,  new OutData(true,"查询成功",dailyHazardService.getEntity(id)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }
}