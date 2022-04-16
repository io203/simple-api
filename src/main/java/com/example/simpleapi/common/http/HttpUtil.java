package com.example.simpleapi.common.http;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtil {
    public static String getRequestBody(HttpServletRequest request) {
        String reqBody = null;
        CachingRequestWrapper wrapper = WebUtils.getNativeRequest(request,
                CachingRequestWrapper.class);
        if (wrapper != null) {
            if (wrapper.getContentLength() > 0) {
                try {
                    reqBody = IOUtils.toString(wrapper.getInputStream(), "UTF-8");
                } catch (IOException e) {
                    log.error(" error message : {}", e.getMessage());
                }
            }
        }
        return reqBody;
    }

    
    public static String getResponseBody(HttpServletResponse response)  {
        String resBody = null;
       
        CachingResponseWrapper wrapper = WebUtils.getNativeResponse(response,CachingResponseWrapper.class);
        if (wrapper != null) {                
            try {
                resBody = IOUtils.toString(wrapper.getContentInputStream(), "UTF-8");
            } catch (IOException e) {                
                e.printStackTrace();
            }
               
        }
        return resBody;
    }

    public static Map getHeaders(HttpServletRequest request) {
        Map headerMap = new HashMap<>();

        Enumeration headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = (String) headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    public static String getParameters(final HttpServletRequest request) {
        final StringBuffer posted = new StringBuffer();
        final Enumeration<?> e = request.getParameterNames();
        if (e != null)
            posted.append("?");
        while (e != null && e.hasMoreElements()) {
            if (posted.length() > 1)
                posted.append("&");
            final String curr = (String) e.nextElement();
            posted.append(curr)
                    .append("=");
            if (curr.contains("password") || curr.contains("answer") || curr.contains("pwd")) {
                posted.append("*****");
            } else {
                posted.append(request.getParameter(curr));
            }
        }

        final String ip = request.getHeader("X-FORWARDED-FOR");
        final String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
        if (!StringUtils.isEmpty(ipAddr))
            posted.append("&_psip=" + ipAddr);
        return posted.toString();
    }

    public static String getRemoteAddr(final HttpServletRequest request) {
        final String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            log.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }

  
    
    
}
