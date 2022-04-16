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
        List<String> targetFiledList = Arrays.asList(SECU_FIELD);

        JsonNode rootNode = node.at("/service_response");

        // Iterator<String> fieldNames = rootNode.fieldNames();
        // while(fieldNames.hasNext()) {
        //     String fieldName = fieldNames.next();        
        //     JsonNode fieldNode = rootNode.get(fieldName);
        //     encryptJson(fieldNode, targetFiledList);
        // }

        encryptJson(rootNode, targetFiledList);
        return node.toPrettyString();

    }
    private static void  encryptJson( JsonNode node, List<String> targetFiledList) {
        if(node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode arrayElement = arrayNode.get(i);
                encryptField(arrayElement,targetFiledList );
            }
        }else{
            encryptField(node,targetFiledList );

        }
    }

    private static void encryptField(JsonNode node, List<String> targetFiledList) {
        for(String targetName :  targetFiledList){
           JsonNode targetNode =  node.get(targetName);
           if(targetNode !=null){
                String value = targetNode.asText();
                ObjectNode targetObjNode = (ObjectNode)node;
                targetObjNode.put(targetName, encryptString(value));
           }
        }
        
    }
    private static String encryptString(String value){
        return "*****encrypt("+value+")******";

    }
    
}
