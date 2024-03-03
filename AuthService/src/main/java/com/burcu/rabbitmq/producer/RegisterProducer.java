package com.burcu.rabbitmq.producer;


import com.burcu.rabbitmq.model.RegisterMailModel;
import com.burcu.rabbitmq.model.RegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterProducer {

    @Value("${rabbitmq.auth-exchange}")
    private String EXCHANGE_AUTH;

    @Value("${rabbitmq.binding-key-register}")
    private String BINDING_KEY_REGISTER;

    @Value("${rabbitmq.binding-key-mail}")
    private String BINDING_KEY_MAIL;


    private final RabbitTemplate  rabbitTemplate;

    public void sendNewUser(RegisterModel model){
        rabbitTemplate.convertAndSend(EXCHANGE_AUTH, BINDING_KEY_REGISTER, model);
    }
    public void sendNewMail(RegisterMailModel model){
        rabbitTemplate.convertAndSend(EXCHANGE_AUTH, BINDING_KEY_MAIL, model);
    }

}

