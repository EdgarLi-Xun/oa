package com.sega.application.oa.config;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * 类功能说明: 消息的基础设置
 * 类修改者	创建日期2020/10/28
 * 修改说明
 *
 * @author wzy
 * @version V1.0
 * @description 说明：
 **/
public class SendMailBasePropertyConfig {

      private static final JavaMailSenderImpl JMS = new JavaMailSenderImpl();
      private static final String HOST = "smtp.exmail.qq.com";
      private static final Integer Port = 465;
      private static final String ENCODING = "Utf-8";
      private static final String AUTH ="mail.smtp.auth";
      private static final String SSL ="mail.smtp.ssl.enable";

      public SendMailBasePropertyConfig(String email,String password){
        JMS.setHost(HOST);
        JMS.setPort(Port);
        JMS.setUsername(email);
        JMS.setPassword(password);
        JMS.setDefaultEncoding(ENCODING);
        Properties p = new Properties();
        p.setProperty(AUTH, "true");
        p.setProperty(SSL, "true");
        JMS.setJavaMailProperties(p);
      }

      public  JavaMailSenderImpl createJMS(){
        return JMS;
      }
}
