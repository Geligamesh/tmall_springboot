package com.gxb.tmall.config;

import com.gxb.tmall.interceptor.LoginInterceptor;
import com.gxb.tmall.interceptor.OtherInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Bean
    public LoginInterceptor getLoginInterceptor(){
        return new LoginInterceptor();
    }

    @Bean
    public OtherInterceptor getOtherInterceptor(){
        return new OtherInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getOtherInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/**");
    }
}
