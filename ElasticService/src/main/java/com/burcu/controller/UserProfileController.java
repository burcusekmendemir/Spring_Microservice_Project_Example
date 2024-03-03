package com.burcu.controller;

import com.burcu.domain.UserProfile;
import com.burcu.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.burcu.constants.RestApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping(FIND_ALL)
    public ResponseEntity<Iterable<UserProfile>> findAll(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

}
