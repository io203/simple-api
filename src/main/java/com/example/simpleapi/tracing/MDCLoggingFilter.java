package com.example.simpleapi.tracing;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;


@Component
public class MDCLoggingFilter implements Filter {
    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;    
        String trxId =  GenerateTrxId.getTrxId(request);
        MDC.put("trxId", trxId);
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();
    }
}
    

