package com.burcu.repository;

import com.burcu.entity.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FollowRepository extends MongoRepository<Follow, String> {
    Optional<Follow> findByFollowingUsersIdAndFollowedUsersId(String id, String id1);
}
