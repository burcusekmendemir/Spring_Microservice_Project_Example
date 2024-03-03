package com.burcu.repository;

import com.burcu.entity.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeRepository extends MongoRepository<Like,String> {
    Boolean existsByUserIdAndPostId(String userId, String postId);

    Boolean existsByUserIdAndCommentId(String userId, String commentId);
}
