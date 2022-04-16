package com.example.simpleapi.config;



import com.example.simpleapi.common.filter.RequestResponseWrapperFilter;
import com.example.simpleapi.common.filter.ResponseBodyEncryptFilter;
import com.example.simpleapi.interceptor.LoggingHttpInterceptor;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

 
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       
        registry .addInterceptor(new LoggingHttpInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/system/*"); 
    }

    /*
    Blocking SpringBoot의  Interceptor에서  RequestBody, ResponseBody를 얻기 위해서 아래를 설정해야 한다
    Reactive SpringBoot은 설정하지 말것
    */
    @Bean
    public RequestResponseWrapperFilter requestResponseWrapperFilter(){
        return new RequestResponseWrapperFilter();
    }

     
    @Bean
    public FilterRegistrationBean<ResponseBodyEncryptFilter> responseBodyEncryptFilter(){
        FilterRegistrationBean<ResponseBodyEncryptFilter> registrationBean =  new FilterRegistrationBean<>();
        registrationBean.setFilter(new ResponseBodyEncryptFilter());
        
        return registrationBean;
    }




}
