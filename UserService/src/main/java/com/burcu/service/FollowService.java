package com.burcu.service;

import com.burcu.dto.request.CreateFollowRequestDto;

import com.burcu.entity.Follow;
import com.burcu.entity.UserProfile;
import com.burcu.exception.ErrorType;
import com.burcu.exception.UserManagerException;
import com.burcu.repository.FollowRepository;
import com.burcu.utility.JwtTokenManager;
import com.burcu.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class FollowService extends ServiceManager<Follow, String> {
    private final FollowRepository followRepository;
    private final JwtTokenManager jwtTokenManager;

    private final UserProfileService userProfileService;

    public FollowService(FollowRepository followRepository, JwtTokenManager jwtTokenManager, UserProfileService userProfileService) {
        super(followRepository);
        this.followRepository = followRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.userProfileService = userProfileService;
    }

    public Object createFollow(CreateFollowRequestDto dto) {
        Follow follow;
        Optional<Long> authId=jwtTokenManager.getIdFromToken(dto.getToken());

        if (authId.isEmpty()){
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile=userProfileService.findByAuthId(authId.get());
        Optional<UserProfile> followedUser=userProfileService.findById(dto.getFollowedUsersId());

        Optional<Follow> followDb=followRepository.
                findByFollowingUsersIdAndFollowedUsersId(userProfile.get().getId(),followedUser.get().getId());

        if (followDb.isPresent()){
            throw new UserManagerException(ErrorType.FOLLOW_ALREADY_EXIST);
        }

        if (userProfile.isPresent() && followedUser.isPresent()){
            follow=Follow.builder()
                    .followedUsersId(dto.getFollowedUsersId())
                    .followingUsersId(userProfile.get().getId()).build();
            followRepository.save(follow);
            userProfile.get().getFollows().add(followedUser.get().getId());
            followedUser.get().getFollowers().add(userProfile.get().getId());
            userProfileService.update(userProfile.get());
            userProfileService.update(followedUser.get());
        }else{
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        return  true;
    }


}
