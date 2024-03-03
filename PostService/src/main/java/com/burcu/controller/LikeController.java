package com.burcu.controller;

import com.burcu.dto.request.CreateLikeRequestDto;
import com.burcu.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.burcu.constants.RestApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(LIKE)
public class LikeController {

    private final LikeService likeService;

}
