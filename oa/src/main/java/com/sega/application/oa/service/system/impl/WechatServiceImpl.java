package com.sega.application.oa.service.system.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sega.application.oa.entity.system.WechatEntity;
import com.sega.application.oa.service.system.IWechatService;
import com.sega.application.oa.utils.HttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class WechatServiceImpl implements IWechatService{

    @Value("${wechat.corpsecret}")
    private String corpsecret;

    @Value("${wechat.tokenUrl}")
    private String tokenUrl;

    @Value("${wechat.corpid}")
    private String corpid;

    @Value("${wechat.approvalUrl}")
    private String approvalUrl;

    /**
     * 根据实体类查询微信消息
     *
     * @param wechatEntity
     * @return
     */
    public JSONArray queryWechatData(WechatEntity wechatEntity){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //记录开始时间
        Calendar startCalendar = Calendar.getInstance();
        //记录截止时间
        Calendar endCalendar = Calendar.getInstance();
        //用于对比查询时间范围是否超过30天
        Calendar contrastCalendar = Calendar.getInstance();
        //获取企业微信的token
        String tokenResult = HttpRequest.sendGet(tokenUrl,"corpid=" + corpid + "&corpsecret=" + corpsecret);
        String token = JSONObject.parseObject(tokenResult).get("access_token").toString();
        //企业微信的返回结果
        JSONArray dataArr = new JSONArray();
        //企业微信的查询条件
        JSONObject  queryObj = new JSONObject();
        //最后返回的接口
        JSONArray returnData = new JSONArray();
        try {
            //微信消息接口有限制，必须传入开始时间和结束时间，如果没有时间，后台直接设置一个查询范围为30天的数据
            if(wechatEntity == null){
                wechatEntity = new WechatEntity();
            }
            if(StringUtils.isNotBlank(wechatEntity.getEndTime())){
                //传入的截止日期加上24小时减一秒，截止到当天最后一秒
                 endCalendar.setTime(new Date(Long.valueOf(wechatEntity.getEndTime() + "000")));
                 endCalendar.add(Calendar.DAY_OF_MONTH,1);
                 endCalendar.add(Calendar.SECOND,-1);
            }
            if(StringUtils.isBlank(wechatEntity.getStartTime())){
                //最初数据从19年10月份开始
                startCalendar.setTime(simpleDateFormat.parse("2019-10-01"));
            } else {
                Date sDate = new Date(Long.valueOf(wechatEntity.getStartTime() + "000"));
                startCalendar.setTime(sDate);
            }
            contrastCalendar.setTime(startCalendar.getTime());
            contrastCalendar.add(Calendar.DAY_OF_MONTH,30);
            while (contrastCalendar.before(endCalendar)) {
                //开始调用微信接口查询
                queryObj.put("starttime", Long.valueOf(startCalendar.getTimeInMillis() / 1000));
                queryObj.put("endtime",Long.valueOf(contrastCalendar.getTimeInMillis() / 1000));
                String result = HttpRequest.sendPost(approvalUrl + token,queryObj.toJSONString());
                JSONObject resultData = JSONObject.parseObject(result);
                dataArr.addAll(resultData.getJSONArray("data"));
                //循环查询所有数据  ，可能存在问题 目前数据量不够，无法进行接口测试
                while (resultData.getJSONArray("data").size() == 100){
                    queryObj.put("next_spnum",resultData.get("next_spnum"));
                    result = HttpRequest.sendPost(approvalUrl + token,queryObj.toJSONString());
                    dataArr.addAll(resultData.getJSONArray("data"));
                }
                startCalendar.setTime(contrastCalendar.getTime());
                startCalendar.add(Calendar.SECOND,1);
                contrastCalendar.add(Calendar.DAY_OF_MONTH,30);
            }
            queryObj.put("starttime", Long.valueOf(startCalendar.getTimeInMillis() / 1000));
            queryObj.put("endtime",Long.valueOf(endCalendar.getTimeInMillis() / 1000));
            String result = HttpRequest.sendPost(approvalUrl + token,queryObj.toJSONString());
            JSONObject resultData = JSONObject.parseObject(result);
            dataArr.addAll(resultData.getJSONArray("data"));
            //循环查询所有数据  ，可能存在问题 目前数据量不够，无法进行接口测试
            while (resultData.getJSONArray("data").size() == 100){
                queryObj.put("next_spnum",resultData.get("next_spnum"));
                result = HttpRequest.sendPost(approvalUrl + token,queryObj.toJSONString());
                dataArr.addAll(resultData.getJSONArray("data"));
            }

            //对返回的结果进行处理 主要是进行过滤
            for(Object object : dataArr){
                JSONObject jsonObject = (JSONObject) object;
                if(StringUtils.isNotBlank(wechatEntity.getApplyName()) &&  jsonObject.get("apply_name").toString().indexOf(wechatEntity.getApplyName()) == -1){
                    continue;
                }
                if(StringUtils.isNotBlank(wechatEntity.getSpName()) &&  !wechatEntity.getSpName().equals(jsonObject.get("spname"))){
                    continue;
                }
                if(StringUtils.isNotBlank(wechatEntity.getSpNum()) &&  !wechatEntity.getSpNum().equals(jsonObject.get("sp_num"))){
                    continue;
                }
                returnData.add(jsonObject);
            }
            return returnData;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }
}
