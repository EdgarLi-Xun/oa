package com.sega.application.oa.controller.humanEngineering;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.annotation.LogInfo;
import com.sega.application.oa.annotation.Module;
import com.sega.application.oa.service.humanengineering.IAttendanceRecordService;
import com.sega.application.oa.entity.humanEngineering.AttendanceRecordEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.sega.application.oa.entity.system.OutData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Date;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sega.application.oa.controller.system.BaseController;
import com.alibaba.fastjson.JSONArray;
import org.springframework.web.multipart.MultipartFile;

/**
 * 管理控制层
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2020-1-8 15:57:37<br/>
 * 历史修订：<br/>
 */
@RestController
@CrossOrigin
@RequestMapping("/apis/humanEngineering/attendanceRecord")
public class AttendanceRecordController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(AttendanceRecordController.class);
	/**
	 * 注入业务层
	 */	
	@Autowired
	private IAttendanceRecordService attendanceRecordService;
	
	/**
     *新增或更新数据
     * @param attendanceRecord
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public void saveOrUpdate(@RequestBody AttendanceRecordEntity attendanceRecord, HttpServletResponse response, HttpServletRequest request) {
        try {
        	if (attendanceRecord.getArId() == null) {
                attendanceRecord.setArCreateDate(new Date());
                attendanceRecord.setArCreateBy(this.getUserBySession(request).getUserId());
        		attendanceRecordService.saveEntity(attendanceRecord);
                this.outJson(response, new OutData(true,"新增成功",attendanceRecord), "yyyy-MM-dd");
        	}else{
                attendanceRecordService.updateEntity(attendanceRecord);
                attendanceRecord.setArUpdateBy(this.getUserBySession(request).getUserId());
                attendanceRecord.setArUpdateDate(new Date());
                this.outJson(response, new OutData(true,"更新成功",attendanceRecord), "yyyy-MM-dd");
        	}
        }catch (Exception e){
            log.error(e.getMessage(),e);
            if (attendanceRecord.getArId() == null) {
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
			AttendanceRecordEntity attendanceRecord = new AttendanceRecordEntity();
            attendanceRecord.setArId(tid);
            attendanceRecord.setArDel(1);
            attendanceRecord.setArUpdateDate(new Date());
            attendanceRecordService.updateEntity(attendanceRecord);
			this.outJson(response, new OutData(false,"删除成功",attendanceRecord), "yyyy-MM-dd");
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
                AttendanceRecordEntity attendanceRecord = new AttendanceRecordEntity();
                attendanceRecord.setArId(tid);
                attendanceRecord.setArDel(1);
                attendanceRecord.setArUpdateDate(new Date());
                attendanceRecordService.updateEntity(attendanceRecord);
            }
			this.outJson(response, new OutData(false,"删除成功"), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}
	
	/**
     * 按条件分页查询数据
     * @param  attendanceRecord
     * @return
     */
    @PostMapping("/queryByPage")
    @ResponseBody
    public void queryByPage(@RequestBody AttendanceRecordEntity attendanceRecord, HttpServletResponse response, HttpServletRequest request) {
        try {

            if (attendanceRecord.getPageNum() == null) {
                attendanceRecord.setPageNum(0);
            }
            if (attendanceRecord.getPageSize() == null) {
               attendanceRecord.setPageSize(20);
            }
            PageHelper.startPage(attendanceRecord.getPageNum(),attendanceRecord.getPageSize());
            List<AttendanceRecordEntity> list = attendanceRecordService.query(attendanceRecord);
            this.outJson(response,  new OutData(true,"查询成功",new PageInfo<AttendanceRecordEntity>(list)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }
    
    /**
     * 按条件查询数据
     * @param attendanceRecord
     * @return
     */
    @PostMapping("/queryAll")
    @ResponseBody
    public void queryAll(@RequestBody AttendanceRecordEntity attendanceRecord, HttpServletResponse response, HttpServletRequest request) {
        try {
            List<AttendanceRecordEntity> list = attendanceRecordService.query(attendanceRecord);
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
            this.outJson(response,  new OutData(true,"查询成功",attendanceRecordService.getEntity(id)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }


    /**
     * 根据ID插入数据
     *
     * @param
     * @return
     */
    @PostMapping("/importExcel")
    @ResponseBody
    public void importExcel(@RequestBody String id, HttpServletResponse response, HttpServletRequest request) {
        try {
            List<Long> ids = JSONArray.parseArray(id,Long.class);
            this.outJson(response,  new OutData(true,"上传成功"), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"上传失败"), "yyyy-MM-dd");
        }
    }


}