package com.sega.application.oa.utils;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.Arrays;

public class StringUtil {

	/**
	 * 字符串MD5加密
	 * @param str
	 * @return
	 */
	public static String MD5(String str) {
		SimpleHash result = new SimpleHash("MD5", str, null, 1);
		// System.out.println(result);
		return result.toString();
	}

	/**
	 * 字符串首字母大写
	 * @param str
	 * @return
	 */
	public static String upperCase(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] -= 32;
		}
		return new String(ch);
	}

	public static Long[] splitStringToLongArray(String str){
	  String[] arr = str.split(",");
	  return (Long[]) ConvertUtils.convert(arr, Long.class);
  }
}
