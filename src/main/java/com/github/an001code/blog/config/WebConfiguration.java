package com.github.an001code.blog.config;



import com.github.an001code.blog.interceptor.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器注册类
 */

@Configuration
@Slf4j
public class WebConfiguration implements WebMvcConfigurer {
    @Autowired
    LoginInterceptor loginInterceptor;

   @Override
   public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
    }
}
