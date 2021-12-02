package com.sega.application.oa.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class SpringUtil {

	/**
	 * 获取当前客户端HttpServletRequest对象
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		try {
			HttpServletRequest request =
					((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			return request;
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
	}
}
