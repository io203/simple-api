package com.example.simpleapi.service;

import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import com.example.simpleapi.model.CreateSimple;
import com.example.simpleapi.model.DeleteSimple;
import com.example.simpleapi.model.UpdateSimple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@KafkaListener(id = "simple-api", topics = "${kafka.topic.name}", errorHandler = "simpleMsgErrorHandler")
@SendTo("simpe-topic2.DLT")
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {
    
    

    @KafkaHandler
    public void handleSimple(CreateSimple createSimple, Acknowledgment acknowledgment) {
        createSimple(createSimple);
        throw new KafkaException("무슨무슨 에러가 발생하였다..!!");   
        // acknowledgment.acknowledge();
    }

    @KafkaHandler
    public void handleSimple(UpdateSimple updateSimple, Acknowledgment acknowledgment) {
        updateSimple(updateSimple);
        acknowledgment.acknowledge();
    }

    @KafkaHandler
    public void handleSimple(DeleteSimple deleteSimple, Acknowledgment acknowledgment) {
        deleteSimple(deleteSimple);
        acknowledgment.acknowledge();
    }

    private void createSimple(CreateSimple createSimple) {
        log.info("==== createSimple {}", createSimple);
        
    }


    private void updateSimple(UpdateSimple updateSimple) {
        log.info("==== updateSimple {}", updateSimple);
    }

    private void deleteSimple(DeleteSimple deleteSimple) {
        log.info("==== deleteSimple {}", deleteSimple);
    }

    

    @KafkaHandler(isDefault = true)
    public void unknown(Object object) {
        System.out.println("======= Unkown type received: " + object);
    }

}
