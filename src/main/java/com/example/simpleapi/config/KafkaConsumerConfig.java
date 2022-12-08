package com.example.simpleapi.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import org.springframework.util.backoff.FixedBackOff;

import com.example.simpleapi.model.CreateSimple;
import com.example.simpleapi.model.DeleteSimple;
import com.example.simpleapi.model.UpdateSimple;

import lombok.extern.slf4j.Slf4j;

@EnableKafka
@Configuration
@Slf4j
public class KafkaConsumerConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;



    // @Value(value = "${spring.kafka.consumer.group-id}")
    // private String groupId;

    // @Bean
    // public ConsumerFactory<String, String> consumerFactory() {
    // Map<String, Object> props = new HashMap<>();
    // props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapAddress);
    // props.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
    // props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
    // StringDeserializer.class);
    // props.put(
    // ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
    // return new DefaultKafkaConsumerFactory<>(props);
    // }

    // @Bean
    // public ConcurrentKafkaListenerContainerFactory<String, String>
    // kafkaListenerContainerFactory() {

    // ConcurrentKafkaListenerContainerFactory<String, String> factory = new
    // ConcurrentKafkaListenerContainerFactory<>();
    // factory.setConsumerFactory(consumerFactory());
    // return factory;
    // }
    // @Bean
    // public KafkaListenerErrorHandler kafkaErrorHandler() {
    // return (m, e) -> {
    // /**
    // * error 로그 기록
    // */
    // log.error("=================================[KafkaErrorHandler]
    // kafkaMessage=[" + m.getPayload()
    // + "], errorMessage=[" + e.getMessage() + "]");

    // return m.getPayload();
    // // 메시지를 더 가공하거나 별도 처리를 하고..

    // // return record; // sendTo("토픽명")에 입력된 토픽으로 전달 될 메시지 내용
    // };
    // }

    @Bean
    public RecordMessageConverter multiTypeConverter() {
        StringJsonMessageConverter converter = new StringJsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        typeMapper.addTrustedPackages("com.example.simpleapi.model");
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("createSimple", CreateSimple.class);
        mappings.put("updateSimple", UpdateSimple.class);
        mappings.put("deleteSimple", DeleteSimple.class);

        typeMapper.setIdClassMapping(mappings);
        converter.setTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public ConsumerFactory<String, Object> multiTypeConsumerFactory() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> multiTypeKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(multiTypeConsumerFactory());
        factory.setMessageConverter(multiTypeConverter());
        return factory;
    }

    

    @Bean
    public DefaultErrorHandler erroHandler(KafkaOperations<String, Object> template, AppKafkaProperties props) {
        return new DefaultErrorHandler(new DeadLetterPublishingRecoverer(template,
                (rec, ex) -> {
                    org.apache.kafka.common.header.Header  retries = rec.headers().lastHeader("retries");
                    if (retries == null) {
                        retries = new RecordHeader("retries", new byte[] { 1 });
                        rec.headers().add(retries);
                    } else {
                        retries.value()[0]++;
                    }
                    return retries.value()[0] > 3
                            ? new TopicPartition(props.topic().name()+props.deadletter().suffix(), rec.partition())
                            : new TopicPartition(props.topic().name(), rec.partition());
                }), new FixedBackOff(props.backoff().initialInterval(), props.backoff().maxInterval()));
    }
}
