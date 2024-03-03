package com.burcu.manager;

import com.burcu.domain.UserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.burcu.constants.RestApiUrls.*;

import java.util.List;

import static com.burcu.constants.RestApiUrls.FIND_ALL;


@FeignClient(url = "http://localhost:7071/dev/v1/user-profile", name = "elastic-userprofile")
public interface UserManager {
     @GetMapping(FIND_ALL)
     ResponseEntity<List<UserProfile>> findAll();
}
