package com.burcu.manager;


import com.burcu.dto.response.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(url = "http://localhost:7071/dev/v1/user-profile", name = "post-userprofile")
public interface UserManager {

    @GetMapping("/find-by-token")
    ResponseEntity<UserResponseDto> findByToken(@RequestParam String token);


}
