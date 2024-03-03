package com.burcu.repository;


import com.burcu.domain.UserProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserProfileRepository extends ElasticsearchRepository<UserProfile, String> {
}
