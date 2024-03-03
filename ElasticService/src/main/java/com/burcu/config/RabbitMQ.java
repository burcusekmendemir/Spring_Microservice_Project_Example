package com.burcu.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQ {

    @Value("${rabbitmq.queue-elastic-register}")
    private String QUEUE_ELASTIC_REGISTER;

    @Bean
    Queue queueElastic(){
        return new Queue(QUEUE_ELASTIC_REGISTER);
    }
}
