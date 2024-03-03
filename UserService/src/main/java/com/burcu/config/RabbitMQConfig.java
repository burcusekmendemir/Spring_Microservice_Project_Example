package com.burcu.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange-user}")
    private String EXCHANGE_USER;

    @Value("${rabbitmq.queue-register}")
    private String QUEUE_REGISTER;
    @Value("${rabbitmq.binding-key-elastic-register}")
    private String BINDING_KEY_ELASTIC_REGISTER;

    @Value("${rabbitmq.queue-elastic-register}")
    private String QUEUE_ELASTIC_REGISTER;
    @Bean
    Queue queueRegister(){
        return new Queue(QUEUE_REGISTER);
    }

    @Bean
    public DirectExchange exchangeUser(){
        return new DirectExchange(EXCHANGE_USER);
    }

    @Bean Queue queueElasticRegister(){
        return new Queue(QUEUE_ELASTIC_REGISTER);
    }
    @Bean
    public Binding bindingElasticRegister(final Queue queueElasticRegister, final DirectExchange exchangeUser){
        return BindingBuilder.bind(queueElasticRegister).to(exchangeUser).with(BINDING_KEY_ELASTIC_REGISTER);

    }


}

