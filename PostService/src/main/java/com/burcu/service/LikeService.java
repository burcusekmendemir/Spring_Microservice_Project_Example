package com.burcu.service;


import com.burcu.entity.Like;
import com.burcu.repository.LikeRepository;
import com.burcu.utility.ServiceManager;
import org.springframework.stereotype.Service;



@Service
public class LikeService extends ServiceManager<Like,String> {
    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        super(likeRepository);
        this.likeRepository=likeRepository;
    }


    public Boolean existsByUserIdAndPostId(String userId, String postId) {
        return likeRepository.existsByUserIdAndPostId(userId,postId);
    }

    public Boolean existsByUserIdAndCommentId(String userId, String commentId) {
        return likeRepository.existsByUserIdAndCommentId(userId,commentId);
    }
}
