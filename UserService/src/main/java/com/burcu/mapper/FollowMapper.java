package com.burcu.mapper;


import com.burcu.dto.request.CreateFollowRequestDto;
import com.burcu.entity.Follow;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FollowMapper {

    FollowMapper INSTANCE= Mappers.getMapper(FollowMapper.class);
    Follow fromFollowRequestToFollow(final CreateFollowRequestDto dto, final String followingUsersId);
}