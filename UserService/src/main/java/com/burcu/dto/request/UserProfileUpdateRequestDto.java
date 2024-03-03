package com.burcu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserProfileUpdateRequestDto {

    private String token; //authla eşleştirmeyi kontrol edicez
    private String email;
    private String phone;
    private String avatar;
    private String address;
    private String about;
}
