package com.burcu.rabbitmq.consumer;


import com.burcu.entity.UserProfile;
import com.burcu.mapper.UserProfileMapper;
import com.burcu.rabbitmq.model.RegisterModel;
import com.burcu.service.UserProfileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterConsumer {


    private final UserProfileService userProfileService;


    @RabbitListener(queues = "${rabbitmq.queue-register}")
    public void createUserListener(RegisterModel model){
        log.info("User {}",model.toString());
        userProfileService.createUserWithRabbitMq(model);
    }


}
