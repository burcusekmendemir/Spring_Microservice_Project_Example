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

public class ElasticModel implements Serializable {


     Long authId;
     String username;
     String email;
     String phone;
     String avatar;
     String address;
     String about;
     EStatus status;

}
