package com.example.simpleapi.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.simpleapi.common.http.CachingRequestWrapper;
import com.example.simpleapi.common.http.CachingResponseWrapper;

import org.springframework.web.filter.OncePerRequestFilter;


public class RequestResponseWrapperFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(new CachingRequestWrapper(request),new CachingResponseWrapper(response));
            // filterChain.doFilter(new CachingRequestWrapper(request),response);
        }
    }
}