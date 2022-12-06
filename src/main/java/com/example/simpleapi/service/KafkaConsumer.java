package com.example.simpleapi.service;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.simpleapi.model.CreateSimple;
import com.example.simpleapi.model.DeleteSimple;
import com.example.simpleapi.model.UpdateSimple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@KafkaListener(id = "simpleGroup", topics = "${kafka.topic.name}")
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {
    
    

    @KafkaHandler
    public void handleSimple(CreateSimple createSimple) {
        createSimple(createSimple);
    }

    @KafkaHandler
    public void handleSimple(UpdateSimple updateSimple) {
        updateSimple(updateSimple);
    }

    @KafkaHandler
    public void handleSimple(DeleteSimple deleteSimple) {
        deleteSimple(deleteSimple);
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
