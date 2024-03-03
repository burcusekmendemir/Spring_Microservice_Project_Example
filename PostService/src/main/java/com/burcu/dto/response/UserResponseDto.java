package com.burcu.dto.response;

import com.burcu.utility.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserResponseDto {

    private String id;
    private Long authId;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private String address;
    private String about;

    @Builder.Default
    private EStatus status = EStatus.PENDING;
    @Builder.Default
    private List<String> follows=new ArrayList<>();
    @Builder.Default
    private List<String> followers=new ArrayList<>();

}
