package com.example.simpleapi.tracing;

import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenerateTrxId {
    public static String getTrxId(HttpServletRequest httpServletRequest) {
        String trxId = httpServletRequest.getHeader("x-trxId");
        if (trxId == null) {
            trxId =   "trxId-"+UUID.randomUUID().toString();            
            log.trace("Generated Trx Id: {}", trxId);
        } 
        return trxId;
    }

    
    
}
