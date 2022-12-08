package com.example.simpleapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.example.simpleapi.config.AppKafkaProperties;
import com.example.simpleapi.model.CreateSimple;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DeadTopicConsumer {
  

    @KafkaListener(topics = "${app.kafka.topic.name}.${app.kafka.deadletter.suffix}")
    @KafkaHandler
    public void deadTopicConsumer(CreateSimple createSimple, Acknowledgment acknowledgment) {
        log.info("### ========= [dead-topic] record: " + createSimple.toString());
        // log.info("### [dead-topic] topic: " + record.topic() + ", value: " +
        // record.value() + ", offset: " + record.offset());

        // kafka 메시지 읽어온 곳까지 commit. (이 부분을 하지 않으면 메시지를 소비했다고 commit 된 것이 아니므로 계속 메시지를
        // 읽어온다)
        acknowledgment.acknowledge();
    }

}
