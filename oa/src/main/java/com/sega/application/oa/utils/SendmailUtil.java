package com.sega.application.oa.utils;

import com.sega.application.oa.config.SendMailBasePropertyConfig;
import com.sega.application.oa.entity.system.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.util.List;

public class SendmailUtil {

  private static final Logger log = LoggerFactory.getLogger(AesUtil.class);

  /**
   * 发送邮件
   */
  public static String sendmail(UserEntity entity, String content, String title, String jsr) throws Exception {

    if (org.springframework.util.StringUtils.isEmpty(entity.getEmailPassword())) {
      throw new Exception("邮箱密码为空");
    }
    SendMailBasePropertyConfig mailConfig = new SendMailBasePropertyConfig(entity.getUserEmail(), entity.getEmailPassword());
    JavaMailSenderImpl jms = mailConfig.createJMS();

    MimeMessage message = jms.createMimeMessage();
    // true表示需要创建一个multipart message
    MimeMessageHelper helper = null;
    // String sjr = "zhangshangao@segasoft.cn";
    helper = new MimeMessageHelper(message, true);

    helper.setFrom(entity.getUserEmail());


    helper.setTo(jsr);

    // 抄送人
    // String csr = "zhangshangao@segasoft.cn";
    // helper.setCc(csr);

    helper.setSubject(title);
    helper.setText(content, true);

    // 发送信息
    jms.send(message);
    return null;
  }

  public static String sendmails(UserEntity entity, String content, String title, List<String> jsrs, List<String> csrs) throws Exception {

    if (org.springframework.util.StringUtils.isEmpty(entity.getEmailPassword())) {
      throw new Exception("邮箱密码为空");
    }
    SendMailBasePropertyConfig mailConfig = new SendMailBasePropertyConfig(entity.getUserEmail(), entity.getEmailPassword());
    JavaMailSenderImpl jms = mailConfig.createJMS();

    MimeMessage message = jms.createMimeMessage();
    // true表示需要创建一个multipart message
    MimeMessageHelper helper = null;
    // String sjr = "zhangshangao@segasoft.cn";
    helper = new MimeMessageHelper(message, true);

    helper.setFrom(entity.getUserEmail());

    String[] jsrss = jsrs.toArray(new String[0]);

    helper.setTo(jsrss);

    // 抄送人
    // String csr = "zhangshangao@segasoft.cn";
    // helper.setCc(csr);
    if (csrs.size() > 0) {
      String[] csrss = csrs.toArray(new String[0]);
      helper.setCc(csrss);
    }

    helper.setSubject(title);
    helper.setText(content, true);

    try {
      // 发送信息
      jms.send(message);
    } catch (MailSendException me) {
      ReportFormUtil.detectInvalidAddress(me);
    }
    return null;
  }

  public static void sendmails(UserEntity entity, String content, ByteArrayInputStream inputstream, String title, List<String> jsrs, List<String> csrs, String fileName) throws Exception {

    if (org.springframework.util.StringUtils.isEmpty(entity.getEmailPassword())) {
      throw new Exception("邮箱密码为空");
    }
    SendMailBasePropertyConfig mailConfig = new SendMailBasePropertyConfig(entity.getUserEmail(), entity.getEmailPassword());
    JavaMailSenderImpl jms = mailConfig.createJMS();

    MimeMessage message = jms.createMimeMessage();
    // true表示需要创建一个multipart message
    MimeMessageHelper helper = null;
    // String sjr = "zhangshangao@segasoft.cn";
    helper = new MimeMessageHelper(message, true);

    helper.setFrom(entity.getUserEmail());

    String[] jsrss = jsrs.toArray(new String[0]);

    helper.setTo(jsrss);

    // 抄送人
    // String csr = "zhangshangao@segasoft.cn";
    // helper.setCc(csr);
    if (csrs.size() > 0) {
      String[] csrss = csrs.toArray(new String[0]);
      helper.setCc(csrss);
    }
    MimeBodyPart contentPart = (MimeBodyPart) createContent(content, inputstream, fileName);

    helper.setSubject(title);
    MimeMultipart mime = new MimeMultipart("mixed");
    mime.addBodyPart(contentPart);
    message.setContent(mime);

    try {
      // 发送信息
      jms.send(message);
    } catch (MailSendException me) {
      ReportFormUtil.detectInvalidAddress(me);
    }
  }

  static Part createContent(String content, ByteArrayInputStream inputstream, String affixName) {
    MimeBodyPart contentPart = null;
    try {
      contentPart = new MimeBodyPart();
      MimeMultipart contentMultipart = new MimeMultipart("related");
      MimeBodyPart htmlPart = new MimeBodyPart();
      htmlPart.setContent(content, "text/html;charset=gbk");
      contentMultipart.addBodyPart(htmlPart);
      //附件部分
      MimeBodyPart excelBodyPart = new MimeBodyPart();
      DataSource dataSource = new ByteArrayDataSource(inputstream, "application/excel");
      DataHandler dataHandler = new DataHandler(dataSource);
      excelBodyPart.setDataHandler(dataHandler);
      excelBodyPart.setFileName(MimeUtility.encodeText(affixName));
      contentMultipart.addBodyPart(excelBodyPart);
      contentPart.setContent(contentMultipart);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return contentPart;
  }
}
