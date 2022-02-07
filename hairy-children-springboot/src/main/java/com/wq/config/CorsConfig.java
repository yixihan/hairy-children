package com.wq.config;

import com.wq.common.PhotoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

/**
 * @author : yixihan
 * @create : 2022-02-05-12:14
 */
@Configuration
public class CorsConfig extends WebMvcConfigurationSupport {

    @Resource
    private PhotoProperties photoProperties;

    /**
     * 解决跨域问题
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry
                // 项目中的所有接口都支持跨域
                .addMapping("/**")
                // 可以访问的具体地址
                .allowedOrigins("http://localhost:9001")
                // 是否允许请求带有验证信息
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 跨域允许时间
                .maxAge(3600);
    }

    /**
     * 解决资源过滤问题
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // swagger
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/**");
        // webjars
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/**");
        registry.addResourceHandler("/druid/**")
                .addResourceLocations("classpath:/META-INF/resources/**");
        // 头像储存位置
        registry.addResourceHandler("/avatar/**")
                .addResourceLocations("file:" + photoProperties.getPaths () + photoProperties.getAvatarPaths ());
        // 线索图片 储存位置
        registry.addResourceHandler("/clue/**")
                .addResourceLocations("file:" + photoProperties.getPaths () + photoProperties.getCluePaths ());
        // 领养图片 储存位置
        registry.addResourceHandler("/adopt/**")
                .addResourceLocations("file:" + photoProperties.getPaths () + photoProperties.getAdoptPaths ());
        // 文章图片 储存位置
        registry.addResourceHandler("/title/**")
                .addResourceLocations("file:" + photoProperties.getPaths () + photoProperties.getTitlePaths ());
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        // 跨域的配置信息   springframework.web.cors.reactive 包下的
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 支持哪些来源的请求
        corsConfiguration.addAllowedOrigin("*");
        // 支持的请求头
        corsConfiguration.addAllowedHeader("*");
        // 支持哪些请求方式
        corsConfiguration.addAllowedMethod("*");
        // 是否允许携带 cookie
        corsConfiguration.setAllowCredentials(true);

        // 注册跨域的配置信息，任意路径下
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(corsConfigurationSource);
    }
}
