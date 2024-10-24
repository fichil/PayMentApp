package com.cykj.config;

import com.cykj.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源访问路径
        registry.addResourceHandler("/imgs/**")
                // classpath下的静态资源目录
                .addResourceLocations("classpath:/template/")
                // 本地磁盘下的静态资源目录
                .addResourceLocations("file:D:\\images\\");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/login","/admin/code","/merchant/login","/merchant/code","/merchant/uploadMerchantIDCardFront","/merchant/uploadMerchantIDCardReverse","/merchant/uploadMerchantLicense");
    }
}
