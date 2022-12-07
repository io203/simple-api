package com.example.simpleapi.service;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.example.simpleapi.model.CreateSimple;
import com.example.simpleapi.model.DeleteSimple;
import com.example.simpleapi.model.UpdateSimple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service

@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {
    
    @KafkaListener(topics = "${kafka.topic.name}")
    public void messageListener(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        log.info("### record: " + record.toString());
        log.info("### topic: " + record.topic() + ", value: " + record.value() + ", offset: " + record.offset());

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

    

}
