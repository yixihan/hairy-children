package com.wq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger 配置类
 * @author : yixihan
 * @create : 2022-02-05-12:09
 */
@Configuration
public class SwaggerConfig {

    /**
     * 配置 Swagger 的 Docket 实例
     */
    @Bean
    public Docket getDocket () {
        return new Docket (DocumentationType.OAS_30)
                .apiInfo (apiInfo ());
    }


    /**
     * 设置 Swagger 接口文档信息
     */
    public ApiInfo apiInfo() {

        // 作者信息
        Contact authorInfo = new Contact("wq", "", "");

        return new ApiInfoBuilder ()
                // 设置标题
                .title ("毛孩纸之家接口文档")
                // 设置描述
                .description ("")
                // 设置作者信息
                .contact (authorInfo)
                .build ();
    }
}
