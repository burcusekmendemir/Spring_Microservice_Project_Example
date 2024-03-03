package com.burcu.rabbitmq.model;


import com.burcu.utility.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterElasticModel implements Serializable {


     private String id;
     private Long authId;
     private String username;
     private String email;
     private String phone;
     private String avatar;
     private String address;
     private String about;
     private EStatus status;

}
