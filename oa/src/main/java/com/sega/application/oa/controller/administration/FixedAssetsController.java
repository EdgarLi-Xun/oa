package com.sega.application.oa.controller.administration;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.controller.system.BaseController;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.administration.IFixedAssetsService;
import com.sega.application.oa.entity.administration.FixedAssetsEntity;
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
@RequestMapping("/apis/fixedAssets")
public class FixedAssetsController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(FixedAssetsController.class);
	/**
	 * 注入业务层
	 */	
	@Autowired
	private IFixedAssetsService fixedAssetsService;

    @Autowired
    private IUserService userService;
	
	/**
     *新增或更新数据
     * @param fixedAssets
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public void saveOrUpdate(@RequestBody FixedAssetsEntity fixedAssets, HttpServletResponse response, HttpServletRequest request) {
        try {
            UserEntity user=(UserEntity) userService.getEntity(fixedAssets.getFaResponsibleId());
            fixedAssets.setFaResponsibleName(user.getUserName());
        	if (fixedAssets.getFaId() == null) {
                fixedAssets.setFaCreateDate(new Date());
                fixedAssets.setFaCreateBy(this.getUserBySession(request).getUserId());
        		fixedAssetsService.saveEntity(fixedAssets);
                this.outJson(response, new OutData(true,"新增成功",fixedAssets), "yyyy-MM-dd");
        	}else{
                fixedAssetsService.updateEntity(fixedAssets);
                fixedAssets.setFaUpdateBy(this.getUserBySession(request).getUserId());
                fixedAssets.setFaUpdateDate(new Date());
                this.outJson(response, new OutData(true,"更新成功",fixedAssets), "yyyy-MM-dd");
        	}
        }catch (Exception e){
            log.error(e.getMessage(),e);
            if (fixedAssets.getFaId() == null) {
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
			FixedAssetsEntity fixedAssets = new FixedAssetsEntity();
            fixedAssets.setFaId(tid);
            fixedAssets.setFaDel(1);
            fixedAssets.setFaUpdateDate(new Date());
            fixedAssetsService.updateEntity(fixedAssets);
			this.outJson(response, new OutData(false,"删除成功",fixedAssets), "yyyy-MM-dd");
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
	public void delete( @RequestBody String tids, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
		try {
            List<Long> ids = JSONArray.parseArray(tids,Long.class);
            for(Long tid : ids){
                FixedAssetsEntity fixedAssets = new FixedAssetsEntity();
                fixedAssets.setFaId(tid);
                fixedAssets.setFaDel(1);
                fixedAssets.setFaUpdateDate(new Date());
                fixedAssetsService.updateEntity(fixedAssets);
            }
			this.outJson(response, new OutData(false,"删除成功"), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}
	
	/**
     * 按条件分页查询数据
     * @param  fixedAssets
     * @return
     */
    @PostMapping("/queryByPage")
    @ResponseBody
    public void queryByPage(@RequestBody FixedAssetsEntity fixedAssets, HttpServletResponse response, HttpServletRequest request) {
        try {

            if (fixedAssets.getPageNum() == null) {
                fixedAssets.setPageNum(0);
            }
            if (fixedAssets.getPageSize() == null) {
               fixedAssets.setPageSize(20);
            }
            PageHelper.startPage(fixedAssets.getPageNum(),fixedAssets.getPageSize());
            List<FixedAssetsEntity> list = fixedAssetsService.query(fixedAssets);
            this.outJson(response,  new OutData(true,"查询成功",new PageInfo<FixedAssetsEntity>(list)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }

    /**
     * 分页查询统计数据
     * @param
     * @return
     */
    @PostMapping("/queryStatistics")
    @ResponseBody
    public void queryStatistics(@RequestBody Map<String,Object> map,HttpServletResponse response){
        try{
            Integer pageNum,pageSize;
            if(map == null){
                pageNum = 0;
                pageSize = 20;
            }else{
                 pageNum = (Integer) map.get("pageNum");
                 pageSize = (Integer) map.get("pageSize");
            }
            PageHelper.startPage(pageNum,pageSize);
            List<Map<String,Object>> list = fixedAssetsService.queryStatistics();
            this.outJson(response,new OutData(true,"查询成功",new PageInfo<Map<String,Object>>(list)),"yyyy-MM-dd");
        }catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }
    /**
     * 按条件查询数据
     * @param fixedAssets
     * @return
     */
    @PostMapping("/queryAll")
    @ResponseBody
    public void queryAll(@RequestBody FixedAssetsEntity fixedAssets, HttpServletResponse response, HttpServletRequest request) {
        try {
            List<FixedAssetsEntity> list = fixedAssetsService.query(fixedAssets);
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
            this.outJson(response,  new OutData(true,"查询成功",fixedAssetsService.getEntity(id)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }
}