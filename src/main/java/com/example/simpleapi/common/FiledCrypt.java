package com.example.simpleapi.common;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FiledCrypt {
    
    private static ObjectMapper mapper;
    private static final String[] SECU_FIELD = {"juminNo","cardNo","telNo","srcId","businessKey"};

    public static ObjectMapper getInstanceMapper(){
        if(mapper == null){
            log.debug("=====call new mapper()");
            mapper =  new ObjectMapper();            
        }
        return mapper;
    }

    // public static String encryptBody(String responseBody){
    //     JsonNode node;
    //     try {
    //         node = getInstanceMapper().readTree(responseBody);
    //     } catch (JsonProcessingException e) {
    //         e.printStackTrace();
    //         return "";
    //     }
    //     List<String> targetFiledList = Arrays.asList(SECU_FIELD);

    //     JsonNode rootNode = node.at("/service_response");

    //     Iterator<String> fieldNames = rootNode.fieldNames();
    //     while(fieldNames.hasNext()) {
    //         String fieldName = fieldNames.next();        
    //         JsonNode fieldNode = rootNode.get(fieldName);
    //         encryptJson(fieldNode, targetFiledList);
    //     }
    //     return node.toPrettyString();

    // }
     public static String encryptBody(String responseBody){
        JsonNode node;
        try {
            node = getInstanceMapper().readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
        List<String> secureFiledList = Arrays.asList(SECU_FIELD);

        JsonNode rootNode = node.at("/service_response");

        Iterator<String> fieldNames = rootNode.fieldNames();
        fieldNames.forEachRemaining(fieldName -> {              
            encryptJson( rootNode.get(fieldName), secureFiledList);
        });
        
        return node.toPrettyString();

    }
    private static void  encryptJson( JsonNode node, List<String> secureFiledList) {
        if(node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            arrayNode.forEach(filedNode -> {
                encryptField(filedNode, secureFiledList );
            });
           
        }else{
            encryptField(node,secureFiledList );
        }
    }

    private static void encryptField(JsonNode node, List<String> secureFiledList) {
        for(String secureFiledName :  secureFiledList){
           JsonNode targetNode =  node.get(secureFiledName);
           if(targetNode !=null){
                String value = targetNode.asText();
                ObjectNode targetObjNode = (ObjectNode)node;
                targetObjNode.put(secureFiledName, encryptString(value));
           }
        }
        
    }
    private static String encryptString(String value){
        return "*****encrypt("+value+")******";

    }
    
}
