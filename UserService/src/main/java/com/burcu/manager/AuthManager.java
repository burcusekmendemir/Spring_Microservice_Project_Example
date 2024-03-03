package com.burcu.manager;

import com.burcu.dto.request.UpdateEmailRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.burcu.constants.RestApiUrls.UPDATE_EMAIL;

@FeignClient(url = "http://localhost:7070/dev/v1/auth", name = "userprofile-auth")
public interface AuthManager {

    @PutMapping(UPDATE_EMAIL)
    ResponseEntity<Boolean> updateEmail(@RequestBody UpdateEmailRequestDto dto);

    @GetMapping("/find-by-role")
   ResponseEntity<List<Long>> findByRole(@RequestParam String role);

}
