package com.javablog.smsplatform.search.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 在线文档配置
 * <p>
 * 访问网址： 1.http://ip:端口/api 或 2.http://ip:端口/swagger-ui.html
 *
 * @author：qianfeng
 * @version: v1.0
 * @date: 2019/3/9 14:31
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    @Value("${swagger.basePackage}")
    private String basePackage;// 扫描controler的包名

    @Value("${swagger.title}")
    private String title;// 在线文档的标题

    @Value("${swagger.description}")
    private String description;// 在线文档的描述

    @Value("${swagger.contact}")
    private String contact;// 联系人

    @Value("${swagger.version}")
    private String version;// 文档版本

    @Bean
    public Docket createRedisApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()// 函数返回一个ApiSelectorBuilder实例用来控制哪些接口暴露给Swagger来展现
                .apis(RequestHandlerSelectors.basePackage(basePackage)).paths(PathSelectors.any()).build();
    }

    /**
     * apiInfo()用来创建该Api的基本信息
     * </p>
     * 这些基本信息会展现在文档页面中
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(title).description(description).contact(contact).version(version)
                .build();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/v1/api-docs", "/v1/api-docs");
        // 添加服务api访问文档url
        registry.addRedirectViewController("/api", "/swagger-ui.html");
    }
}
