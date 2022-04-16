package com.example.simpleapi.common.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.example.simpleapi.common.http.ResponseBodyEncryptWrapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

        String jsonBody = new String(responseWrapper.getDataStream(), StandardCharsets.UTF_8);
        
        String modifyMessage = modifyJsonBody(jsonBody);
        log.info("=====ResponseBodyEncryptFilt3333er====== {}", modifyMessage);

        response.setContentLength(modifyMessage.length());
        response.getOutputStream().write(modifyMessage.getBytes());

    }

    private String modifyJsonBody(String jsonBody) {
        JsonNode node;
        try {
            node = mapper.readTree(jsonBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
        List<String> targetFiledList = Arrays.asList("secretBase","secretIdentity","name");

        JsonNode rootNode = node.at("/service_response");

        Iterator<String> fieldNames = rootNode.fieldNames();
        while(fieldNames.hasNext()) {
            String fieldName = fieldNames.next();        
            JsonNode fieldNode = rootNode.get(fieldName);
            encryptBody(fieldNode, targetFiledList);
        }
        return node.toPrettyString();
    }
    private void  encryptBody( JsonNode node, List<String> targetFiledList) {
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

    private void encryptField(JsonNode node, List<String> targetFiledList) {
        for(String targetName :  targetFiledList){
           JsonNode targetNode =  node.get(targetName);
           if(targetNode !=null){
                String value = targetNode.asText();
                ObjectNode targetObjNode = (ObjectNode)node;
                targetObjNode.put(targetName, encryptString(value));
           }
        }
        
    }
    private String encryptString(String value){
        return "encrypt("+value+")";

    }

    private void printAllJsonNodes(String nodeFieldName, JsonNode node) {
        if (node.isObject()) {
            Iterator<String> fieldNames = node.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode fieldValue = node.get(fieldName);
                printAllJsonNodes(fieldName, fieldValue);
            }

        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode arrayElement = arrayNode.get(i);
                printAllJsonNodes(nodeFieldName, arrayElement);
            }
        } else {
            
            if("secretIdentity".equals(nodeFieldName)){
                String value = node.asText();
                ObjectNode objNode =  (ObjectNode)node;
                String encryptVlaue = value+"encrypt data";
                objNode.put(nodeFieldName, encryptVlaue);

            }else{
                String value = node.asText();
                log.info("{} = {}", nodeFieldName, value);

            }
            

        }

    }

    private void test(String rootFieldName, JsonNode root) {
        log.info("==[root]rootFieldName: {}  ", rootFieldName);

        if (root.isObject()) {
            log.info("======== rootFieldName :{} isObject: true  ", rootFieldName);
            printObject(root);
        } else if (root.isArray()) {
            log.info("====== rootFieldName :{} isArray: true  ", rootFieldName);
            ArrayNode arrayNode = (ArrayNode) root;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode arrayElement = arrayNode.get(i);
                if (arrayElement.isObject()) {
                    printObject(arrayElement);

                } else {
                    printArrayValue(rootFieldName, arrayElement);
                    // log.info("value : {}", arrayElement.asText());
                }

            }
        } else {
            log.info("====== rootFieldName :{} isMember: true  ", rootFieldName);
            String value = root.get(rootFieldName).asText();
            log.info("{} = {}", rootFieldName, value);

        }

    }

    private void printObject(JsonNode node) {
        Iterator<String> fieldNames = node.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode valueNode = node.get(fieldName);
            if (valueNode.isArray()) {
                printArrayValue(fieldName, valueNode);
            } else {
                String fieldValue = node.get(fieldName).asText();
                log.info("{} = {} ", fieldName, fieldValue);
            }
        }
    }

    private void printArrayValue(String fieldName, JsonNode node) {
        log.info("{} [", fieldName);
        ArrayNode arrayNode = (ArrayNode) node;
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode arrayElement = arrayNode.get(i);
            log.info("   {}", arrayElement.asText());
        }
        log.info(" ]");
    }

    private void searchJson(String json) throws IOException {
        JsonFactory jfactory = new JsonFactory();
        JsonParser jParser = jfactory.createParser(json);

        String parsedName = null;
        Integer parsedAge = null;
        List<String> addresses = new LinkedList<>();
        while (jParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldname = jParser.getCurrentName();
            if ("name".equals(fieldname)) {
                jParser.nextToken();
                parsedName = jParser.getText();

            }

            if ("age".equals(fieldname)) {
                jParser.nextToken();
                parsedAge = jParser.getIntValue();
            }

            if ("address".equals(fieldname)) {
                jParser.nextToken();
                while (jParser.nextToken() != JsonToken.END_ARRAY) {
                    addresses.add(jParser.getText());
                }
            }
        }
        jParser.close();

        log.info("====== parsedName : {}", parsedName);
        log.info("====== parsedAge : {}", parsedAge);
        log.info("====== addresses : {}", addresses.toString());

    }

    private void searchOneJson(String json) throws IOException {
        JsonFactory jfactory = new JsonFactory();

        JsonParser jParser = jfactory.createParser(json);
        Integer parsedAge = null;
        while (jParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldname = jParser.getCurrentName();

            if ("age".equals(fieldname)) {
                jParser.nextToken();
                parsedAge = jParser.getIntValue();
                return;
            }

        }
        jParser.close();
    }

    private void printElement(JsonNode node) {
        if (node.isArray()) {

            for (JsonNode arrayElement : node) {
                Iterator<Entry<String, JsonNode>> iterator = arrayElement.fields();

                while (iterator.hasNext()) {
                    log.info("key: {}", iterator.next().getKey());
                    log.info("value: {}", iterator.next().getValue().asText());
                }
            }

        } else {

            log.info("value: {}", node.asText());
        }

    }

}
