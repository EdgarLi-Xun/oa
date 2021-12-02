package com.sega.application.oa.service.system;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sega.application.oa.entity.system.WechatEntity;

/**
 *  微信消息接口
 * @author 孙乾
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2019-10-24 17:02:21<br/>
 * 历史修订：<br/>
 */
public interface IWechatService {

    /**
     * 根据实体类查询微信消息
     * @param wechatEntity
     * @return
     */
    public JSONArray queryWechatData(WechatEntity wechatEntity);
}
