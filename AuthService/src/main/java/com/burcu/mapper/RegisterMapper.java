package com.burcu.mapper;

import com.burcu.dto.request.ActivateStatusRequestDto;
import com.burcu.dto.request.CreateUserRequestDto;
import com.burcu.dto.request.RegisterRequestDto;
import com.burcu.dto.response.RegisterResponseDto;
import com.burcu.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegisterMapper {

    RegisterMapper INSTANCE= Mappers.getMapper(RegisterMapper.class);

    Auth fromRegisterRequestDtoToAuth(final RegisterRequestDto dto);
    RegisterResponseDto fromAuthToRegisterResponseDto(final Auth auth);

    @Mapping(source = "id",target = "authId") //dto ve authtaki idler farklı isimdeydi
    CreateUserRequestDto fromAuthToCreateUserRequestDto(final Auth auth);

    ActivateStatusRequestDto fromAuthToActivateStatusRequestDto(final Auth auth);


}
