package com.microservice.kafka.kafka.service;

public interface KafkaService {

    void publishMessage(String topicName,String key, Object message);


}
