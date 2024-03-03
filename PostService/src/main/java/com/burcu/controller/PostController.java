package com.burcu.controller;

import com.burcu.dto.request.CreateCommentLikeRequestDto;
import com.burcu.dto.request.CreateCommentRequestDto;
import com.burcu.dto.request.CreateLikeRequestDto;
import com.burcu.dto.request.CreatePostRequestDto;
import com.burcu.entity.Post;
import com.burcu.service.CommentService;
import com.burcu.service.LikeService;
import com.burcu.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.burcu.constants.RestApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(POST)
public class PostController {

    private final PostService postService;

    @PostMapping(ADD)
    public ResponseEntity<Post> createPost(@RequestBody CreatePostRequestDto dto){
        return ResponseEntity.ok(postService.createPost(dto));
    }

    @PostMapping(POST_LIKE)
    public ResponseEntity<Post> postLike(@RequestBody CreateLikeRequestDto dto){
        return ResponseEntity.ok(postService.postLike(dto));
    }
    @PostMapping(POST_COMMENT)
    public ResponseEntity<Post> postComment(@RequestBody CreateCommentRequestDto dto){
        return ResponseEntity.ok(postService.postComment(dto));
    }




}
