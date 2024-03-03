package com.burcu.controller;

import com.burcu.dto.request.*;
import com.burcu.dto.response.UserResponseDto;
import com.burcu.entity.UserProfile;
import com.burcu.service.UserProfileService;

import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.burcu.constants.RestApiUrls.*;


@RestController
@RequestMapping(USER_PROFILE)
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createUser(@RequestBody CreateUserRequestDto dto){
        return ResponseEntity.ok(userProfileService.createUser(dto));
    }

    @GetMapping(ACTIVATE_STATUS+"/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.activateStatus(authId));
    }

    @GetMapping(FIND_ALL)
    public ResponseEntity<List<UserProfile>> findAll(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> update(@RequestBody UserProfileUpdateRequestDto dto){
        return ResponseEntity.ok(userProfileService.update(dto));
    }


    @DeleteMapping(DELETE_BY_TOKEN)
    public ResponseEntity<Boolean> deleteByToken(@RequestParam String token){
        return ResponseEntity.ok(userProfileService.softDeleteByToken(token));
    }

    @GetMapping("/find-by-user-name")
    public ResponseEntity<UserProfile> findByUserName(@RequestParam String userName){
        return ResponseEntity.ok( userProfileService.findByUserName(userName));
    }

    @GetMapping("/find-by-role")
    public ResponseEntity<List<UserProfile>> findByRole(@RequestParam String role){
        return ResponseEntity.ok(userProfileService.findByRole(role));
    }


    @GetMapping("/find-by-token")
    public ResponseEntity<UserResponseDto> findByToken(@RequestParam String token){
        return ResponseEntity.ok(userProfileService.findByToken(token));
    }




}
