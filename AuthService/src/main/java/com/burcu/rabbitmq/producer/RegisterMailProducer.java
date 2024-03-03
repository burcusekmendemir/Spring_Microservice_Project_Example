package com.burcu.rabbitmq.producer;


import com.burcu.rabbitmq.model.RegisterMailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterMailProducer {

    @Value("${rabbitmq.auth-exchange}")
    private String EXCHANGE_AUTH;

    @Value("${rabbitmq.binding-key-mail}")
    private String BINDING_KEY_MAIL;

    private final RabbitTemplate  rabbitTemplate;


    public void sendActivationCode(RegisterMailModel model){
        rabbitTemplate.convertAndSend(EXCHANGE_AUTH, BINDING_KEY_MAIL, model);
    }
}

