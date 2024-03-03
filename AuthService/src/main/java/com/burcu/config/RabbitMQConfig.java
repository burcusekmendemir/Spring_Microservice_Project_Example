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

    @Value("${rabbitmq.auth-exchange}")
    private String EXCHANGE_AUTH;
     @Value("${rabbitmq.queue-register}")
    private String QUEUE_REGISTER;
     @Value("${rabbitmq.binding-key-register}")
    private String BINDING_KEY_REGISTER;
     @Value("${rabbitmq.queue-mail}")
    private String QUEUE_MAIL;
     @Value("${rabbitmq.binding-key-mail}")
    private String BINDING_KEY_MAIL;

    @Value("${rabbitmq.queue-elastic}")
    private String QUEUE_ELASTIC;
    @Value("${rabbitmq.binding-key-elastic}")
    private String BINDING_KEY_ELASTIC;


    @Bean
    DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE_AUTH);
    }

    @Bean
    Queue queueRegister(){
        return new Queue(QUEUE_REGISTER);
    }

    @Bean
    Binding bindingRegister(final DirectExchange directExchange,final Queue queueRegister){
        return BindingBuilder.bind(queueRegister).to(directExchange).with(BINDING_KEY_REGISTER);
    }

    @Bean
    Queue queueRegisterMail(){
        return new Queue(QUEUE_MAIL);
    }

    @Bean
    Binding bindingRegisterMail(final DirectExchange directExchange,final Queue queueRegisterMail){
        return BindingBuilder.bind(queueRegisterMail).to(directExchange).with(BINDING_KEY_MAIL);
    }

    @Bean
    Queue queueElastic(){
        return new Queue(QUEUE_ELASTIC);
    }

    @Bean
    Binding bindingElastic(final DirectExchange directExchange,final Queue queueElastic){
        return BindingBuilder.bind(queueElastic).to(directExchange).with(BINDING_KEY_ELASTIC);
    }


}

