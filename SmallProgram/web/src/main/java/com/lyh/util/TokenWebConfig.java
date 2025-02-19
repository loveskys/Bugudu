package com.lyh.util;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class TokenWebConfig implements WebMvcConfigurer {

    @Resource
    private TokenInterceptor tokenInterceptor;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //自定义拦截器，addPathPatterns设置拦截/api/**所有的请求，excludePathPatterns配置白名单路径
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/admin/login",
                        "/api/user/student_login",
                        "/api/user/student_wx_login",
                        "/api/user/student_register",
                        "/api/home/home_list",
                        "/api/home/swiper_list",
                        "/api/wxmsg/checktest"
                );

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 取消默认静态资源处理
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}