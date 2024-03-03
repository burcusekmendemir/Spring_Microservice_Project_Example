package com.burcu.rabbitmq.consumer;


import com.burcu.rabbitmq.model.RegisterMailModel;
import com.burcu.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterMailConsumer {


    private final MailSenderService mailSenderService;


    @RabbitListener(queues = "${rabbitmq.queue-mail}")
    public void sendActivationCodeListener(RegisterMailModel model){
        log.info("Model {}", model.toString());
        mailSenderService.sendMail(model);
    }


}
