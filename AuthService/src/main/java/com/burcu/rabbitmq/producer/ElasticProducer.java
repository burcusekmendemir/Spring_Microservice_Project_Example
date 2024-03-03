package com.burcu.rabbitmq.producer;

import com.burcu.rabbitmq.model.ElasticModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticProducer {

    @Value("${rabbitmq.auth-exchange}")
    private String EXCHANGE_AUTH;

    @Value("${rabbitmq.binding-key-elastic}")
    private String BINDING_KEY_ELASTIC;

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(ElasticModel model){
        rabbitTemplate.convertAndSend(EXCHANGE_AUTH, BINDING_KEY_ELASTIC, model);
    }
}
