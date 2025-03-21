package com.microservice.kafka.kafka.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.kafka.kafka.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer implements KafkaService {

    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducer(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishMessage(String topicName,String key, Object message) {

        try{
        String jsonMessage = objectMapper.writeValueAsString(message);
        kafkaTemplate.send(topicName, key, jsonMessage);
        System.out.println("Message published -->" + message);
        }catch (JsonProcessingException e){
            System.err.println("Error converting object to JSON: " + e.getMessage());
            throw new RuntimeException("Error serializing message to JSON", e);
        }
    }

}
