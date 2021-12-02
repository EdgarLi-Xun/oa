package com.sega.application.oa.service.system.impl;

import com.sega.application.oa.dao.system.IBaseDao;

import java.util.Properties;


/**
 * 邮件发送接口
 */
public class EmailServiceImpl  {

    public static void main(String [] args)
    {
        // 收件人电子邮箱
        String to = "abcd@gmail.com";

        // 发件人电子邮箱
        String from = "web@gmail.com";

        // 指定发送邮件的主机为 localhost
        String host = "localhost";

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);


    }

}
