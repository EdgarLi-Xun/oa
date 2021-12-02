//package com.sega.application.oa.config;
//
//import lombok.Data;
//import org.flowable.engine.ProcessEngine;
//import org.flowable.engine.ProcessEngineConfiguration;
//import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
///**
// * 流程引擎配置文件
// * @author: linjinp
// * @create: 2019-10-21 16:49
// **/
//@Configuration
//@ConfigurationProperties(prefix = "spring.datasource")
//@Data
//public class ProcessEngineConfig {
//
//    private Logger logger = LoggerFactory.getLogger(ProcessEngineConfig.class);
//
//    @Value("${spring.datasource.url}")
//    private String url;
//
//    @Value("${spring.datasource.driver-class-name}")
//    private String driverClassName;
//
//    @Value("${spring.datasource.username}")
//    private String username;
//
//    @Value("${spring.datasource.password}")
//    private String password;
//
//
//    @Value("${spring.mail.username}")
//    private String mailAccount;
//
//    @Value("${spring.mail.password}")
//    private String mailPassword;
//
//    @Value("${spring.mail.host}")
//    private String mailHost;
//
//    /**
//     * 初始化流程引擎
//     * @return
//     */
//    @Primary
//    @Bean(name = "processEngine")
//    public ProcessEngine initProcessEngine() {
//        logger.info("=============================ProcessEngineBegin=============================");
//
//        // 流程引擎配置
//        ProcessEngineConfiguration cfg = null;
//
//        try {
//            cfg = new StandaloneProcessEngineConfiguration()
//                    .setJdbcUrl(url)
//                    .setJdbcUsername(username)
//                    .setJdbcPassword(password)
//                    .setJdbcDriver(driverClassName)
//                    // 初始化基础表，不需要的可以改为 DB_SCHEMA_UPDATE_FALSE
//                    .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_DROP_CREATE)
//                    // 默认邮箱配置
//                    // 发邮件的主机地址，先用 QQ 邮箱
//                    .setMailServerHost(mailHost)
//                    // POP3/SMTP服务的授权码
//                    .setMailServerPassword(mailPassword)
//                    // 默认发件人
//                    .setMailServerDefaultFrom(mailAccount)
//                    // 设置发件人用户名
//                    .setMailServerUsername("管理员")
//                    // 解决流程图乱码
//                    .setActivityFontName("宋体")
//                    .setLabelFontName("宋体")
//                    .setAnnotationFontName("宋体");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // 初始化流程引擎对象
//        ProcessEngine processEngine = cfg.buildProcessEngine();
//        logger.info("=============================ProcessEngineEnd=============================");
//        return processEngine;
//    }
//}
//
