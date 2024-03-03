package com.burcu.controller;


import com.burcu.dto.request.CreateCommentLikeRequestDto;
import com.burcu.entity.Comment;
import com.burcu.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.burcu.constants.RestApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(COMMENT)
public class CommentController {

    private final CommentService commentService;

    @PostMapping(COMMENT_LIKE)
    public ResponseEntity<Comment> likeComment(@RequestBody CreateCommentLikeRequestDto dto){
        return ResponseEntity.ok(commentService.commentLike(dto));
    }
}
