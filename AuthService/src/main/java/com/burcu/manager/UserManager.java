package com.burcu.manager;

import com.burcu.dto.request.CreateUserRequestDto;
import com.burcu.dto.request.DeleteRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.burcu.constants.RestApiUrls.*;



@FeignClient(url = "http://localhost:7071/dev/v1/user-profile", name = "auth-userprofile")
public interface UserManager {

    @PostMapping("/create")
    ResponseEntity<Boolean> createUser(@RequestBody CreateUserRequestDto dto);

    @GetMapping(ACTIVATE_STATUS+"/{authId}")
    ResponseEntity<Boolean> activateStatus(@PathVariable Long authId);


    @DeleteMapping(DELETE_BY_TOKEN)
    ResponseEntity<Boolean> deleteByToken(@RequestParam String token);



}
