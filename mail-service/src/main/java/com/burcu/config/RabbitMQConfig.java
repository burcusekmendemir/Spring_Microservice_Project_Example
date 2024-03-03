package com.burcu.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

   @Value("${rabbitmq.queue-mail}")
    private String QUEUE_MAIL;

    @Bean
    Queue queueRegisterMail(){
        return new Queue(QUEUE_MAIL);
    }


}

