package com.burcu.mapper;

import com.burcu.dto.request.CreateUserRequestDto;
import com.burcu.dto.response.UserResponseDto;
import com.burcu.entity.UserProfile;
import com.burcu.rabbitmq.model.RegisterElasticModel;
import com.burcu.rabbitmq.model.RegisterModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileMapper {
    UserProfileMapper INSTANCE= Mappers.getMapper(UserProfileMapper.class);

    UserProfile fromCreateRequestToUserProfile(final CreateUserRequestDto dto);


    UserProfile fromRegisterModelToUserProfile(final RegisterModel model);

    RegisterElasticModel fromUserProfileToRegisterElasticModel(final UserProfile userProfile);

    UserResponseDto fromUserProfileToUserProfileResponse(final UserProfile userProfile);
}
