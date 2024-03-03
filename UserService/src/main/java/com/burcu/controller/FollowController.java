package com.burcu.controller;

import com.burcu.dto.request.CreateFollowRequestDto;
import com.burcu.entity.Follow;
import com.burcu.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.burcu.constants.RestApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(FOLLOW)
public class FollowController {
    private final FollowService followService;

    @PostMapping(CREATE)
    public ResponseEntity<?> createFollow(@RequestBody CreateFollowRequestDto dto){
        return ResponseEntity.ok(followService.createFollow(dto));
    }

    @GetMapping(FIND_ALL)
    public  ResponseEntity<List<Follow>> findAll(){
        return  ResponseEntity.ok(followService.findAll());
    }

}
