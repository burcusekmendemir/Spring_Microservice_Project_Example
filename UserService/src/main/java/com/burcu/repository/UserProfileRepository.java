package com.burcu.repository;

import com.burcu.entity.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserProfileRepository extends MongoRepository<UserProfile,String> {
    Optional<UserProfile> findByAuthId(Long authId);

    Optional<UserProfile> findByUsernameIgnoreCase(String userName);



}
