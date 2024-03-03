package com.burcu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients //feign clientla çalışabilecek feign sunucusu
public class AuthServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(AuthServiceApplication.class, args);
    }

}