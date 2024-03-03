package com.burcu.mapper;


import com.burcu.domain.UserProfile;
import com.burcu.rabbitmq.model.RegisterElasticModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ElasticMapper {

    ElasticMapper INSTANCE = Mappers.getMapper(ElasticMapper.class);

    UserProfile fromRegisterElasticModelToUserProfile(final RegisterElasticModel registerElasticModel);
}
