package com.sega.application.oa.utils;

import java.io.BufferedReader;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.net.HttpURLConnection;

import java.net.URL;

/**
 * 判断当前时间是否是工作日
 * 返回 0：上班  1：周末  2：节假日
 *
 */

public class HolidayUtil {
  /**
   * @param :请求接口
   * @param httpArg :参数
   * @return 返回结果
   */

  public static String request(String httpArg) {
    String httpUrl = "http://tool.bitefu.net/jiari/";

    BufferedReader reader = null;

    String result = null;

    StringBuffer sbf = new StringBuffer();

    httpUrl = httpUrl + "?d=" + httpArg;

    try {
      URL url = new URL(httpUrl);

      HttpURLConnection connection = (HttpURLConnection) url

        .openConnection();

      connection.setRequestMethod("GET");

      connection.connect();

      InputStream is = connection.getInputStream();

      reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

      String strRead = null;

      while ((strRead = reader.readLine()) != null) {
        sbf.append(strRead);

      }

      reader.close();

      result = sbf.toString();

    } catch (Exception e) {
      e.printStackTrace();

    }

    return result;

  }
}

