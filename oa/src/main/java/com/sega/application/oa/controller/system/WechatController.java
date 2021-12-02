package com.sega.application.oa.controller.system;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.entity.administration.FixedAssetsEntity;
import com.sega.application.oa.entity.system.OutData;
import com.sega.application.oa.entity.system.WechatEntity;
import com.sega.application.oa.service.system.IWechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/apis/wechat")
public class WechatController extends BaseController  {

    private static final Logger log = LoggerFactory.getLogger(WechatController.class);

    @Autowired
    private IWechatService wechatService;

    /**
     * 查询微信相关数据
     * @param  wechatEntity
     * @return
     */
    @PostMapping("/queryWechatData")
    @ResponseBody
    public void queryWechatData(@RequestBody WechatEntity wechatEntity, HttpServletResponse response, HttpServletRequest request) {
        try {
            JSONArray list = wechatService.queryWechatData(wechatEntity);
            this.outJson(response,  new OutData(true,"查询成功",list), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }
}
