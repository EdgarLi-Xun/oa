package com.sega.application.oa.controller.daily;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.entity.daily.DailyHazardEntity;
import com.sega.application.oa.service.daily.IDailyDetailService;
import com.sega.application.oa.entity.daily.DailyDetailEntity;
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
@RequestMapping("/apis/daily/dailyDetail")
public class DailyDetailController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DailyDetailController.class);
	/**
	 * 注入业务层
	 */	
	@Autowired
	private IDailyDetailService dailyDetailService;
	
	/**
     *新增或更新数据
     * @param dailyDetail
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public void saveOrUpdate(@RequestBody DailyDetailEntity dailyDetail, HttpServletResponse response, HttpServletRequest request) {
        try {
        	if (dailyDetail.getDdId() == null) {
                dailyDetail.setDdCreateDate(new Date());
                dailyDetail.setDdCreateBy(this.getUserBySession(request).getUserId());
        		dailyDetailService.saveEntity(dailyDetail);
                this.outJson(response, new OutData(true,"新增成功",dailyDetail), "yyyy-MM-dd");
        	}else{
                dailyDetailService.updateEntity(dailyDetail);
                dailyDetail.setDdUpdateBy(this.getUserBySession(request).getUserId());
                dailyDetail.setDdUpdateDate(new Date());
                this.outJson(response, new OutData(true,"更新成功",dailyDetail), "yyyy-MM-dd");
        	}
        }catch (Exception e){
            log.error(e.getMessage(),e);
            if (dailyDetail.getDdId() == null) {
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
			DailyDetailEntity dailyDetail = new DailyDetailEntity();
            dailyDetail.setDdId(tid);
            dailyDetail.setDdDel(1);
            dailyDetail.setDdUpdateDate(new Date());
            dailyDetailService.updateEntity(dailyDetail);
			this.outJson(response, new OutData(false,"删除成功",dailyDetail), "yyyy-MM-dd");
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
                DailyDetailEntity dailyDetail = new DailyDetailEntity();
                dailyDetail.setDdId(tid);
                dailyDetail.setDdDel(1);
                dailyDetail.setDdUpdateDate(new Date());
                dailyDetailService.updateEntity(dailyDetail);
            }
			this.outJson(response, new OutData(false,"删除成功"), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}
	
	/**
     * 按条件分页查询数据
     * @param  dailyDetail
     * @return
     */
    @PostMapping("/queryByPage")
    @ResponseBody
    public void queryByPage(@RequestBody DailyDetailEntity dailyDetail, HttpServletResponse response, HttpServletRequest request) {
        try {

            if (dailyDetail.getPageNum() == null) {
                dailyDetail.setPageNum(0);
            }
            if (dailyDetail.getPageSize() == null) {
               dailyDetail.setPageSize(20);
            }
            PageHelper.startPage(dailyDetail.getPageNum(),dailyDetail.getPageSize());
            List<DailyDetailEntity> list = dailyDetailService.query(dailyDetail);
            this.outJson(response,  new OutData(true,"查询成功",new PageInfo<DailyDetailEntity>(list)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }
    
    /**
     * 按条件查询数据
     * @param dailyDetail
     * @return
     */
    @PostMapping("/queryAll")
    @ResponseBody
    public void queryAll(@RequestBody DailyDetailEntity dailyDetail, HttpServletResponse response, HttpServletRequest request) {
        try {
            List<DailyDetailEntity> list = dailyDetailService.query(dailyDetail);
            this.outJson(response,  new OutData(true,"查询成功",list), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }

    /**
     * 根据日报id查询日报详情
     * @param dailyid
     * @return
     */
    @PostMapping("/querydailydetailBydailyid")
    @ResponseBody
    public void querydailydetailBydailyid( @RequestBody  String dailyid, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
        String daid=dailyid.replaceAll("=","");
        DailyDetailEntity dailyDetailEntity=new DailyDetailEntity();
        dailyDetailEntity.setDdDailyId(Long.parseLong(daid));
        List<DailyHazardEntity> list = dailyDetailService.query(dailyDetailEntity);
        this.outJson(response,  new OutData(true,"查询成功",list), "yyyy-MM-dd");
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
            this.outJson(response,  new OutData(true,"查询成功",dailyDetailService.getEntity(id)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }
}