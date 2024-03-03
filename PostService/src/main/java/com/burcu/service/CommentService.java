package com.burcu.service;

import com.burcu.dto.request.CreateCommentLikeRequestDto;
import com.burcu.dto.response.UserResponseDto;
import com.burcu.entity.Comment;
import com.burcu.entity.Like;
import com.burcu.exception.ErrorType;
import com.burcu.exception.PostServiceException;
import com.burcu.manager.UserManager;
import com.burcu.repository.CommentRepository;
import com.burcu.repository.PostRepository;
import com.burcu.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService extends ServiceManager<Comment,String> {
    private final CommentRepository commentRepository;
    private final UserManager userManager;
    private final PostRepository postRepository;
    private final LikeService likeService;


    public CommentService(CommentRepository repository, CommentRepository commentRepository, UserManager userManager, PostRepository postRepository, LikeService likeService) {
        super(repository);
        this.commentRepository = commentRepository;
        this.userManager = userManager;
        this.postRepository = postRepository;
        this.likeService = likeService;
    }



    public Comment commentLike(CreateCommentLikeRequestDto dto) {
        Comment comment = commentRepository.findById(dto.getCommentId()).orElseThrow(()-> new PostServiceException(ErrorType.COMMENT_NOT_FOUND));


        UserResponseDto userProfile = Optional.ofNullable(userManager.findByToken(dto.getToken()).getBody())
                .orElseThrow(()->new PostServiceException(ErrorType.USER_NOT_FOUND));

        Like like = Like.builder()
                .postId(comment.getPostId())
                .userId(userProfile.getId())
                .username(userProfile.getUsername())
                .userAvatar(userProfile.getAvatar())
                .commentId(comment.getId())
                .build();
        if(likeService.existsByUserIdAndCommentId(userProfile.getId(), comment.getId())){
            throw new PostServiceException(ErrorType.LIKE_ALREADY_EXISTS);
        }
        likeService.save(like);
        comment.getCommentLikes().add(like.getCommentId());

        return update(comment);
    }
}
