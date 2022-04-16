package com.example.simpleapi.common.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.example.simpleapi.common.FiledCrypt;
import com.example.simpleapi.common.http.ResponseBodyEncryptWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseBodyEncryptFilter implements Filter {

    
    private ObjectMapper mapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        mapper = new ObjectMapper();
        ResponseBodyEncryptWrapper responseWrapper = new ResponseBodyEncryptWrapper((HttpServletResponse) response);
        chain.doFilter(request, responseWrapper);

        String responseBody = new String(responseWrapper.getDataStream(), StandardCharsets.UTF_8);
        
        String modifyBody = FiledCrypt.encryptBody(responseBody);
        log.info("=====ResponseBodyEncryptFilter====== {}", modifyBody);

        response.setContentLength(modifyBody.length());
        response.getOutputStream().write(modifyBody.getBytes());

    }

    

}
