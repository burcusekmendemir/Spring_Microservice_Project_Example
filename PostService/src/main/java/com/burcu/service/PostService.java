package com.burcu.service;

import com.burcu.dto.request.CreateCommentRequestDto;
import com.burcu.dto.request.CreateLikeRequestDto;
import com.burcu.dto.request.CreatePostRequestDto;
import com.burcu.dto.response.UserResponseDto;
import com.burcu.entity.Comment;
import com.burcu.entity.Like;
import com.burcu.entity.Post;
import com.burcu.exception.ErrorType;
import com.burcu.exception.PostServiceException;
import com.burcu.manager.UserManager;
import com.burcu.mapper.PostMapper;
import com.burcu.repository.PostRepository;
import com.burcu.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService extends ServiceManager<Post,String> {
    private final PostRepository postRepository;
    private final UserManager userManager;
    private final LikeService likeService;
    private final CommentService commentService;

    public PostService(PostRepository postRepository, UserManager userManager, LikeService likeService, CommentService commentService) {
        super(postRepository);
        this.postRepository=postRepository;
        this.userManager = userManager;
        this.likeService = likeService;
        this.commentService = commentService;
    }

    public Post createPost(CreatePostRequestDto dto) {
        UserResponseDto user= userManager.findByToken(dto.getToken()).getBody();
        Post post = PostMapper.INSTANCE.fromCreateRequestToPost(dto);
        post.setUserId(user.getId());
        post.setUsername(user.getUsername());
        post.setUserAvatar(user.getAvatar());
        return postRepository.save(post);


    }

    public Post postLike(CreateLikeRequestDto dto) {

        UserResponseDto userProfile = Optional.ofNullable(userManager.findByToken(dto.getToken()).getBody())
                .orElseThrow(()->new PostServiceException(ErrorType.USER_NOT_FOUND));

        Post post = postRepository.findById(dto.getPostId()).orElseThrow(()->new PostServiceException(ErrorType.POST_NOT_FOUND));


        Like like = Like.builder()
                .postId(post.getId())
                .userId(userProfile.getId())
                .username(userProfile.getUsername())
                .userAvatar(userProfile.getAvatar())
                .build();

        if (likeService.existsByUserIdAndPostId(userProfile.getId(), post.getId())) {
            throw new PostServiceException(ErrorType.LIKE_ALREADY_EXISTS);
        }
        likeService.save(like);
        post.getLikes().add(like.getId());

        return update(post);
    }


    public Post postComment(CreateCommentRequestDto dto) {
        UserResponseDto userProfile = Optional.ofNullable(userManager.findByToken(dto.getToken()).getBody())
                .orElseThrow(()->new PostServiceException(ErrorType.USER_NOT_FOUND));

        Post post = postRepository.findById(dto.getPostId()).orElseThrow(()->new PostServiceException(ErrorType.POST_NOT_FOUND));


        Comment comment = Comment.builder()
                .postId(post.getId())
                .userId(userProfile.getId())
                .username(userProfile.getUsername())
                .userAvatar(userProfile.getAvatar())
                .content(dto.getContent())
                .build();


        commentService.save(comment);
        post.getComments().add(comment.getId());
        return update(post);
    }
}
