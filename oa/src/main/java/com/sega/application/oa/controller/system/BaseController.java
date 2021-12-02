package com.sega.application.oa.controller.system;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.sega.application.oa.base.RedisClient;
import com.sega.application.oa.entity.system.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class BaseController {
	
	protected Logger logger = Logger.getLogger(getClass());

	@Value("${refreshTokenExpireTime}")
	protected String refreshTokenExpireTime;

	@Autowired
	private RedisClient redis;

	/**
	 * 修改spring mvc传输数组长度限制为1024
	 * @param binder
	 */
	@InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(1024);
    }

	/**
	 * 输出json格式的object
	 * @param response HttpServletResponse对象，请求响应对象
	 * @param object 需要json序列化的数据
	 */
	protected void outJson(HttpServletResponse response, Object object) {
		PrintWriter out = null;
		response.setContentType("application/json;charset=utf-8");
		try {
			out = response.getWriter();
			String jsonString = JSON.toJSONString(object);
			out.println(jsonString);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 以dateFormat格式格式化时间，输出json格式的object数据
	 * @param response HttpServletResponse对象，请求响应对象
	 * @param object 需要json序列化的数据
	 * @param dateFormat 日期格式化字符串
	 */
	protected void outJson(HttpServletResponse response, Object object, String dateFormat) {
		PrintWriter out = null;
		response.setContentType("application/json;charset=utf-8");
		try {
			out = response.getWriter();
			String jsonString = JSON.toJSONStringWithDateFormat(object, dateFormat);
			out.println(jsonString);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 输出object字符串
	 * @param response HttpServletResponse对象
	 * @param str 字符串
	 */
	protected void outString(HttpServletResponse response, Object str) {
		PrintWriter out = null;
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			out.println(str);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取session中的用户实体
	 * <p>若没有用户，返回null</p>
	 */
	protected UserEntity getUserBySession(ServletRequest request) {
		try {
			Subject subject = SecurityUtils.getSubject();
			if (subject != null) {
				String token = (String) subject.getPrincipal();
				if (StringUtils.isNotBlank(token)) {
					String userJson = (String)redis.get(token);
					UserEntity user = JSONObject.parseObject(userJson,UserEntity.class);
					if (user != null) {
						return user;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
