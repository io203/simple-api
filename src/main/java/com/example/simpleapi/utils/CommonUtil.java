package com.example.simpleapi.utils;

import java.util.HashMap;
import java.util.Map;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtil {

    private static ObjectMapper objectMapper;
    private static XmlMapper xmlMapper;

    public static ObjectMapper getInstanceObjectMapper(){
        if(objectMapper == null){
            log.debug("=====call new ObjectMapper()");
            objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        }
        return objectMapper;
    }

    public static XmlMapper getInstanceXmlMapper(){
        if(xmlMapper == null){
            log.debug("=====call new XmlMapper()");
            xmlMapper = (XmlMapper) new XmlMapper()
                .registerModule(new JavaTimeModule());
        }
        return xmlMapper;
    }


    
    public static <T> T jsonToObject( String jsonStr,String keyPath, TypeReference<T>  typeReference) {
        ObjectMapper  mapper = getInstanceObjectMapper();
        T objT =null;
        try {
            String json= mapper.readTree(jsonStr)
                .at(keyPath)
                .toString();            
            if(json !=null){
                objT =  mapper.readValue(json, typeReference);
            }            
        } catch (JsonProcessingException e) {               
            e.printStackTrace();          
        }
        return objT;
    
    }

    public static String objectToXml(Object obj){
        XmlMapper xmlMapper = getInstanceXmlMapper();
        String xml = null ;
        try {
            xml = xmlMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {           
            e.printStackTrace();
        }
        
        return xml;
    }
    
}
