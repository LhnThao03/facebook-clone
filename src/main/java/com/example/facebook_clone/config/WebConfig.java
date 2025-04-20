package com.example.facebook_clone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//WebConfig là một lớp cấu hình Spring Boot, dùng để đăng ký Interceptor. 
//Nó sẽ chỉ định bạn muốn LoginInterceptor áp dụng cho những URL nào.
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/home/**")   // chặn các đường dẫn cần đăng nhập
                .excludePathPatterns("/login", "/register", "/css/**", "/js/**", "/images/**"); // cho phép truy cập tự do
    }
}
