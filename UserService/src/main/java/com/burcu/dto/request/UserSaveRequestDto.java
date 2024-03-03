package com.burcu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserSaveRequestDto {

     String id;
     Long authId;
     String username;
     String email;
     String phone;
     String avatar;
     String address;
     String about;

}
