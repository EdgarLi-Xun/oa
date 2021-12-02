package com.sega.application.oa.controller.daily;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.service.daily.IDailyReceivcerService;
import com.sega.application.oa.entity.daily.DailyReceivcerEntity;
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
 * 创建日期：2019-12-18 16:30:25<br/>
 * 历史修订：<br/>
 */
@RestController
@CrossOrigin
@RequestMapping("/apis/daily/dailyReceivcer")
public class DailyReceivcerController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DailyReceivcerController.class);
	/**
	 * 注入业务层
	 */	
	@Autowired
	private IDailyReceivcerService dailyReceivcerService;
	
	/**
     *新增或更新数据
     * @param dailyReceivcer
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public void saveOrUpdate(@RequestBody DailyReceivcerEntity dailyReceivcer, HttpServletResponse response, HttpServletRequest request) {
        try {
        	if (dailyReceivcer.getDrId() == null) {
                dailyReceivcer.setDrCreateDate(new Date());
                dailyReceivcer.setDrCreateBy(this.getUserBySession(request).getUserId());
        		dailyReceivcerService.saveEntity(dailyReceivcer);
                this.outJson(response, new OutData(true,"新增成功",dailyReceivcer), "yyyy-MM-dd");
        	}else{
                dailyReceivcerService.updateEntity(dailyReceivcer);
                dailyReceivcer.setDrUpdateBy(this.getUserBySession(request).getUserId());
                dailyReceivcer.setDrUpdateDate(new Date());
                this.outJson(response, new OutData(true,"更新成功",dailyReceivcer), "yyyy-MM-dd");
        	}
        }catch (Exception e){
            log.error(e.getMessage(),e);
            if (dailyReceivcer.getDrId() == null) {
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
			DailyReceivcerEntity dailyReceivcer = new DailyReceivcerEntity();
            dailyReceivcer.setDrId(tid);
            dailyReceivcer.setDrDel(1);
            dailyReceivcer.setDrUpdateDate(new Date());
            dailyReceivcerService.updateEntity(dailyReceivcer);
			this.outJson(response, new OutData(false,"删除成功",dailyReceivcer), "yyyy-MM-dd");
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
                DailyReceivcerEntity dailyReceivcer = new DailyReceivcerEntity();
                dailyReceivcer.setDrId(tid);
                dailyReceivcer.setDrDel(1);
                dailyReceivcer.setDrUpdateDate(new Date());
                dailyReceivcerService.updateEntity(dailyReceivcer);
            }
			this.outJson(response, new OutData(false,"删除成功"), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}



	/**
     * 按条件分页查询数据
     * @param  dailyReceivcer
     * @return
     */
    @PostMapping("/queryByPage")
    @ResponseBody
    public void queryByPage(@RequestBody DailyReceivcerEntity dailyReceivcer, HttpServletResponse response, HttpServletRequest request) {
        try {

            if (dailyReceivcer.getPageNum() == null) {
                dailyReceivcer.setPageNum(0);
            }
            if (dailyReceivcer.getPageSize() == null) {
               dailyReceivcer.setPageSize(20);
            }
            PageHelper.startPage(dailyReceivcer.getPageNum(),dailyReceivcer.getPageSize());
            List<DailyReceivcerEntity> list = dailyReceivcerService.query(dailyReceivcer);
            this.outJson(response,  new OutData(true,"查询成功",new PageInfo<DailyReceivcerEntity>(list)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }
    
    /**
     * 按条件查询数据
     * @param dailyReceivcer
     * @return
     */
    @PostMapping("/queryAll")
    @ResponseBody
    public void queryAll(@RequestBody DailyReceivcerEntity dailyReceivcer, HttpServletResponse response, HttpServletRequest request) {
        try {
            List<DailyReceivcerEntity> list = dailyReceivcerService.query(dailyReceivcer);
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
            this.outJson(response,  new OutData(true,"查询成功",dailyReceivcerService.getEntity(id)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }
}