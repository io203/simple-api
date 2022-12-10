package com.example.simpleapi.service;

import org.apache.kafka.common.errors.SerializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import com.example.simpleapi.config.AppKafkaProperties;
import com.example.simpleapi.model.CreateSimple;
import com.example.simpleapi.model.DeleteSimple;
import com.example.simpleapi.model.UpdateSimple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@KafkaListener( id = "${spring.kafka.consumer.group-id}",topics = "${app.kafka.topic.name}")
@RequiredArgsConstructor
@Slf4j
// @SendTo("${kafka.topic.name}.DLT")
public class KafkaConsumer {
    @Autowired
    private AppKafkaProperties props;
    
    // public void handleSimple( CreateSimple createSimple, Acknowledgment acknowledgment , @Header(KafkaHeaders.OFFSET) Long offset,
    //                     @Header(KafkaHeaders.CONSUMER) KafkaConsumer consumer,
    //                     @Header(KafkaHeaders.TIMESTAMP_TYPE) String timestampType,
    //                     @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
    //                     @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partitionId,
    //                     @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String messageKey,
    //                     //@Header(value  KafkaHeaders.RECEIVED_MESSAGE_KEY, required  false) Integer messageKey,
    //                     @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp)  {
    

    @RetryableTopic(
        attempts = "5",
        topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
        backoff = @Backoff(delay = 1000, multiplier = 2.0),
        exclude = {SerializationException.class, DeserializationException.class}

    )
    @KafkaHandler   
    public void handleSimple(@Headers MessageHeaders messageHeaders,@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.OFFSET) String offset, CreateSimple createSimple, Acknowledgment acknowledgment) {
        
        String topicName = props.topic().name();
        log.info("=======messageHeaders: {} offset:::::: {} from topic: {}", messageHeaders.toString(),offset, topic);
        // createSimple(createSimple);
        throw new RuntimeException("에러가 발생했다..!!");  
        // acknowledgment.acknowledge();
    }

    @DltHandler
    public void handleDlt(CreateSimple createSimple, Acknowledgment acknowledgment) {
        log.info("=======================Message: {} handled by dlq ",  createSimple.toString());
        acknowledgment.acknowledge();
    }

    // @KafkaHandler
    // public void handleSimple(UpdateSimple updateSimple, Acknowledgment acknowledgment) {
    //     updateSimple(updateSimple);
    //     acknowledgment.acknowledge();
    // }

    // @KafkaHandler
    // public void handleSimple(DeleteSimple deleteSimple, Acknowledgment acknowledgment) {
    //     deleteSimple(deleteSimple);
    //     acknowledgment.acknowledge();
    // }

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
