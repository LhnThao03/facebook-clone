package com.example.facebook_clone.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.facebook_clone.interceptor.AdminInterceptor;
import com.example.facebook_clone.interceptor.LoginInterceptor;


//WebConfig là một lớp cấu hình Spring Boot, dùng để đăng ký Interceptor. 
//Nó sẽ chỉ định bạn muốn LoginInterceptor áp dụng cho những URL nào.
@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	
     // Interceptor kiểm tra đăng nhập
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/home/**", "/profile/**", "/admin/**","/friends/**","/post-detail/**","/posts/**","/profile-view/**","/user-details/**","/user-edit/**")
                .excludePathPatterns("/login", "/register", "/css/**", "/js/**");

        // Interceptor kiểm tra quyền admin
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**");
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/IMG/**")
        .addResourceLocations("file:" + System.getProperty("user.dir") + "/IMG/");
    }
}
