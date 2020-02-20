//package com.qianfeng.smsplatform.userinterface.config;
//
//import SmsServlet;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import javax.servlet.ServletRegistration;
//
//@Configuration
//public class ServletConfig {
//
//    @Bean
//    public ServletRegistrationBean servletRegisterBen() {
//        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(
//                new SmsServlet(), "/receiveSms"
//        );
//        return servletRegistrationBean;
//    }
//}