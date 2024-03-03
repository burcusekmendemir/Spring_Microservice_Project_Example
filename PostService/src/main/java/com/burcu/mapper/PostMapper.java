package com.burcu.mapper;


import com.burcu.dto.request.CreateLikeRequestDto;
import com.burcu.dto.request.CreatePostRequestDto;
import com.burcu.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    Post fromCreateRequestToPost(final CreatePostRequestDto dto);

    Post fromCreateLikeRequestDtoToPost(final CreateLikeRequestDto dto);
}
