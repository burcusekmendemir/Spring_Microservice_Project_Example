package com.burcu.service;

import com.burcu.domain.UserProfile;
import com.burcu.dto.request.UserSaveRequestDto;
import com.burcu.mapper.ElasticMapper;
import com.burcu.rabbitmq.model.RegisterElasticModel;
import com.burcu.repository.UserProfileRepository;
import com.burcu.utility.ServiceManager;
import org.springframework.stereotype.Service;


@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {

    private final UserProfileRepository userProfileRepository;


    public UserProfileService(UserProfileRepository userProfileRepository) {
        super(userProfileRepository);
        this.userProfileRepository=userProfileRepository;
    }

    public UserProfile createUserWithRabbitMq(RegisterElasticModel model) {
        return save(ElasticMapper.INSTANCE.fromRegisterElasticModelToUserProfile(model));
    }



}
