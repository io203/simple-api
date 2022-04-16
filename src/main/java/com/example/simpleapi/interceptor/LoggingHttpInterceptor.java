package com.example.simpleapi.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.simpleapi.common.http.HttpUtil;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingHttpInterceptor implements HandlerInterceptor {

    

    public LoggingHttpInterceptor() {
        // this.monService = (MonitoringService) CommonBeanUtil.getBean(MonitoringService.class);;        
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
           
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        log.info("[postHandle]");
        String body  = HttpUtil.getResponseBody(response);
        // log.info("==========postHandle======body========== {}", body);
        
        // monService.sendResponseData(request, response);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception ex)
            throws Exception {
        log.info("[afterCompletion]");
    }

    
}
