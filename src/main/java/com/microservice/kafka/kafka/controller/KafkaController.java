package com.microservice.kafka.kafka.controller;

import com.microservice.kafka.kafka.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private final KafkaService kafkaService;

    public KafkaController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @PostMapping("/producer/{topic}/key/{key}")
    public ResponseEntity<Mono<?>> publishMessage(@PathVariable String topic, @PathVariable String key, @Valid @RequestBody Object message) {
        Mono.just(kafkaService).doOnNext(t -> {

                    t.publishMessage(topic, key, message);

                }).onErrorReturn(kafkaService).onErrorResume(e -> Mono.just(kafkaService))
                .onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> log.info(x.toString()));
        return new ResponseEntity<>(Mono.just(message), HttpStatus.CREATED);
    }

}
