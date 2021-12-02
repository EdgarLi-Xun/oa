package com.sega.application.oa.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.service.system.IDictService;
import com.sega.application.oa.entity.system.DictEntity;
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
@RequestMapping("/apis/daily/dict")
public class DictController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DictController.class);
	/**
	 * 注入业务层
	 */	
	@Autowired
	private IDictService dictService;
	
	/**
     *新增或更新数据
     * @param dict
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public void saveOrUpdate(@RequestBody DictEntity dict, HttpServletResponse response, HttpServletRequest request) {
        try {
        	if (dict.getDictId() == null) {
                dict.setDictCreateDate(new Date());
                dict.setDictCreateBy(this.getUserBySession(request).getUserId());
        		dictService.saveEntity(dict);
                this.outJson(response, new OutData(true,"新增成功",dict), "yyyy-MM-dd");
        	}else{
                dictService.updateEntity(dict);
                dict.setDictUpdateBy(this.getUserBySession(request).getUserId());
                dict.setDictUpdateDate(new Date());
                this.outJson(response, new OutData(true,"更新成功",dict), "yyyy-MM-dd");
        	}
        }catch (Exception e){
            log.error(e.getMessage(),e);
            if (dict.getDictId() == null) {
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
			DictEntity dict = new DictEntity();
            dict.setDictId(tid);
            dict.setDictDel(1);
            dict.setDictUpdateDate(new Date());
            dictService.updateEntity(dict);
			this.outJson(response, new OutData(false,"删除成功",dict), "yyyy-MM-dd");
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
                DictEntity dict = new DictEntity();
                dict.setDictId(tid);
                dict.setDictDel(1);
                dict.setDictUpdateDate(new Date());
                dictService.updateEntity(dict);
            }
			this.outJson(response, new OutData(false,"删除成功"), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}
	
	/**
     * 按条件分页查询数据
     * @param  dict
     * @return
     */
    @PostMapping("/queryByPage")
    @ResponseBody
    public void queryByPage(@RequestBody DictEntity dict, HttpServletResponse response, HttpServletRequest request) {
        try {

            if (dict.getPageNum() == null) {
                dict.setPageNum(0);
            }
            if (dict.getPageSize() == null) {
               dict.setPageSize(20);
            }
            PageHelper.startPage(dict.getPageNum(),dict.getPageSize());
            List<DictEntity> list = dictService.query(dict);
            this.outJson(response,  new OutData(true,"查询成功",new PageInfo<DictEntity>(list)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }
    
    /**
     * 按条件查询数据
     * @param dict
     * @return
     */
    @PostMapping("/queryAll")
    @ResponseBody
    public void queryAll(@RequestBody DictEntity dict, HttpServletResponse response, HttpServletRequest request) {
        try {
            List<DictEntity> list = dictService.query(dict);
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
            this.outJson(response,  new OutData(true,"查询成功",dictService.getEntity(id)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }
}