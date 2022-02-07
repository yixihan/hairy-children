package com.wq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 配置类
 * @author : yixihan
 * @create : 2022-02-05-12:09
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 配置 Swagger 的 Docket 实例
     */
    @Bean
    public Docket docket () {
        return new Docket(DocumentationType.OAS_30)
                // 创建该 API 的基本信息
                .apiInfo(apiInfo());

    }


    /**
     * 配置 Swagger 的信息
     * @return apiInfo
     */
    private ApiInfo apiInfo () {

        // 作者信息
        Contact authorInfo = new Contact(
                "yixihan",
                "https://github.com/yixihan",
                "3113788997@qq.com"
        );

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
