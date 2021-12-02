package com.sega.application.oa;


import com.sega.application.oa.config.AppDispatcherServletConfiguration;
//import com.sega.application.oa.config.ApplicationConfiguration;
//import com.sega.application.oa.config.DatabaseAutoConfiguration;
//import org.flowable.ui.modeler.conf.DatabaseConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//启用全局异常拦截器
@Import(value={
		// 引入修改的配置
//		ApplicationConfiguration.class,
		AppDispatcherServletConfiguration.class
//
		// 引入 DatabaseConfiguration 表更新转换
//		DatabaseAutoConfiguration.class
})
@ComponentScan(basePackages = {"com.sega.application.oa.*"})
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
// 开启定时
@EnableScheduling
public class myOaApplication {

	public static void main(String[] args){
		SpringApplication.run(myOaApplication.class,args);
	}

}
