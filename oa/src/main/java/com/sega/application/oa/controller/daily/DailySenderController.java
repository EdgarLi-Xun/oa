package com.sega.application.oa.controller.daily;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.service.daily.IDailySenderService;
import com.sega.application.oa.entity.daily.DailySenderEntity;
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
@RequestMapping("/apis/daily/dailySender")
public class DailySenderController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DailySenderController.class);
	/**
	 * 注入业务层
	 */
	@Autowired
	private IDailySenderService dailySenderService;

	/**
     *新增或更新数据
     * @param dailySender
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public void saveOrUpdate(@RequestBody DailySenderEntity dailySender, HttpServletResponse response, HttpServletRequest request) {
        try {
        	if (dailySender.getDsId() == null) {
                dailySender.setDsCreateDate(new Date());
                dailySender.setDsCreateBy(this.getUserBySession(request).getUserId());
        		dailySenderService.saveEntity(dailySender);
                this.outJson(response, new OutData(true,"新增成功",dailySender), "yyyy-MM-dd");
        	}else{
                dailySenderService.updateEntity(dailySender);
                dailySender.setDsUpdateBy(this.getUserBySession(request).getUserId());
                dailySender.setDsUdpateDate(new Date());
                this.outJson(response, new OutData(true,"更新成功",dailySender), "yyyy-MM-dd");
        	}
        }catch (Exception e){
            log.error(e.getMessage(),e);
            if (dailySender.getDsId() == null) {
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
			DailySenderEntity dailySender = new DailySenderEntity();
            dailySender.setDsId(tid);
            dailySender.setDsDel(1);
            dailySender.setDsUdpateDate(new Date());
            dailySenderService.updateEntity(dailySender);
			this.outJson(response, new OutData(false,"删除成功",dailySender), "yyyy-MM-dd");
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
                DailySenderEntity dailySender = new DailySenderEntity();
                dailySender.setDsId(tid);
                dailySender.setDsDel(1);
                dailySender.setDsUdpateDate(new Date());
                dailySenderService.updateEntity(dailySender);
            }
			this.outJson(response, new OutData(false,"删除成功"), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}

	/**
     * 按条件分页查询数据
     * @param  dailySender
     * @return
     */
    @PostMapping("/queryByPage")
    @ResponseBody
    public void queryByPage(@RequestBody DailySenderEntity dailySender, HttpServletResponse response, HttpServletRequest request) {
        try {

            if (dailySender.getPageNum() == null) {
                dailySender.setPageNum(0);
            }
            if (dailySender.getPageSize() == null) {
               dailySender.setPageSize(20);
            }
            PageHelper.startPage(dailySender.getPageNum(),dailySender.getPageSize());
            List<DailySenderEntity> list = dailySenderService.query(dailySender);
            this.outJson(response,  new OutData(true,"查询成功",new PageInfo<DailySenderEntity>(list)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }

    /**
     * 查询发送人邮箱
     * @param username
     * @return
     */
    @PostMapping("/queryjsrEmail")
    @ResponseBody
    public void queryjsrEmail(@RequestBody  String username, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
        try {

            String newnusername=username.split("=")[0];

            List<Map<String, Object>> list = dailySenderService.queryjsrEmail(newnusername);
            this.outJson(response,  new OutData(true,"查询成功",list), "yyyy-MM-dd");
        }catch (RuntimeException e){
            this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
        }
    }

    /**
     * 查询抄送人邮箱
     * @param dailySender
     * @param response
     * @param request
     */
    /**
     * 查询发送人邮箱
     * @param username
     * @return
     */
    @PostMapping("/querycsrEmail")
    @ResponseBody
    public void querycsrEmail(@RequestBody  String username, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
        try {
            String newnusername=username.split("=")[0];

            List<Map<String, Object>> list = dailySenderService.querycsrEmail(newnusername);
            this.outJson(response,  new OutData(true,"查询成功",list), "yyyy-MM-dd");
        }catch (RuntimeException e){
            this.outJson(response, new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }

  /**
   * 查询发送人邮箱
   * @return
   */
  @PostMapping("/loadJsrAndCsrEmail")
  @ResponseBody
  public void loadJsrAndCsrEmail(HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
    try {

      Map<String, Object> map = dailySenderService.queryJsrAndCsrEmail(this.getUserBySession(request).getUserId());
      this.outJson(response,  new OutData(true,"查询成功",map), "yyyy-MM-dd");
    }catch (RuntimeException e){
      this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
    }
  }

    @PostMapping("/querysendByPage")
    @ResponseBody
    public void querysendByPage(@RequestBody DailySenderEntity dailySender, HttpServletResponse response, HttpServletRequest request) {
        try {

            if (dailySender.getPageNum() == null) {
                dailySender.setPageNum(0);
            }
            if (dailySender.getPageSize() == null) {
                dailySender.setPageSize(20);
            }
            PageHelper.startPage(dailySender.getPageNum(),dailySender.getPageSize());
            List<DailySenderEntity> list = dailySenderService.querysendByPage(dailySender);
            this.outJson(response,  new OutData(true,"查询成功",new PageInfo<DailySenderEntity>(list)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }

    /**
     * 按条件查询数据
     * @param dailySender
     * @return
     */
    @PostMapping("/queryAll")
    @ResponseBody
    public void queryAll(@RequestBody DailySenderEntity dailySender, HttpServletResponse response, HttpServletRequest request) {
        try {
            List<DailySenderEntity> list = dailySenderService.query(dailySender);
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
            this.outJson(response,  new OutData(true,"查询成功",dailySenderService.getEntity(id)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }
}
