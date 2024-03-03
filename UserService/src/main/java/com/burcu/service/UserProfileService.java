package com.burcu.service;

import com.burcu.dto.request.*;
import com.burcu.dto.response.UserResponseDto;
import com.burcu.entity.UserProfile;
import com.burcu.exception.ErrorType;
import com.burcu.exception.UserManagerException;
import com.burcu.manager.AuthManager;
import com.burcu.mapper.UserProfileMapper;
import com.burcu.rabbitmq.model.RegisterModel;
import com.burcu.rabbitmq.producer.RegisterElasticProducer;
import com.burcu.repository.UserProfileRepository;
import com.burcu.utility.JwtTokenManager;
import com.burcu.utility.ServiceManager;
import com.burcu.utility.enums.EStatus;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final UserProfileRepository userProfileRepository;
    private final JwtTokenManager jwtTokenManager;
    private final AuthManager authManager;
    private final CacheManager cacheManager;
    private final RegisterElasticProducer registerElasticProducer;

    public UserProfileService(UserProfileRepository repository, JwtTokenManager jwtTokenManager, AuthManager authManager, CacheManager cacheManager, RegisterElasticProducer registerElasticProducer) {
        super(repository);
        this.userProfileRepository = repository;
        this.jwtTokenManager = jwtTokenManager;
        this.authManager = authManager;
        this.cacheManager = cacheManager;
        this.registerElasticProducer = registerElasticProducer;
    }

    public Boolean createUser(CreateUserRequestDto dto) {
        try {
            userProfileRepository.saveAll(List.of());
            save(UserProfileMapper.INSTANCE.fromCreateRequestToUserProfile(dto));
            return true;
        } catch (Exception e) {
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }

    public void createUserWithRabbitMq(RegisterModel model) {
        try {
            UserProfile userProfile = save(UserProfileMapper.INSTANCE.fromRegisterModelToUserProfile(model));
            registerElasticProducer.sendNewUser(UserProfileMapper.INSTANCE.fromUserProfileToRegisterElasticModel(userProfile));
        } catch (Exception e){
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }


    public Boolean activateStatus(Long authId) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByAuthId(authId);
        if (optionalUserProfile.isPresent()) {
            optionalUserProfile.get().setStatus(EStatus.ACTIVE);
            update(optionalUserProfile.get());
            return true;
        } else {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
    }

    public Boolean update(UserProfileUpdateRequestDto dto) {
        Optional<Long> authId = jwtTokenManager.getIdFromToken(dto.getToken()); //dto içindeki tokenla çözümleme yapıyoruz
        if (authId.isEmpty()) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByAuthId(authId.get()); //authid'yle eşleşen userprofile var mı bakıyoruz
        if (optionalUserProfile.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }

        UserProfile userProfile = optionalUserProfile.get();
        userProfile.setAddress(dto.getAddress());
        userProfile.setAbout(dto.getAbout());
        userProfile.setEmail(dto.getEmail());
        userProfile.setPhone(dto.getPhone());
        userProfile.setAvatar(dto.getAvatar());
        update(userProfile);

       authManager.updateEmail(UpdateEmailRequestDto.builder()
                .id(userProfile.getAuthId())
                .email(userProfile.getEmail())
                .build());


        Objects.requireNonNull(cacheManager.getCache("user-find-by-username")).evict(userProfile.getUsername().toLowerCase()); //spesifik username anahtarsa cachein kriterini yazdık.
        Objects.requireNonNull(cacheManager.getCache("user-find-by-role")).clear(); //update edildikten sonra user-find-by-role cacheini sıfırladık.
        return true;
    }


    public Boolean softDeleteByToken(String token){
        Optional<Long> authId = jwtTokenManager.getIdFromToken(token);
        if (authId.isEmpty()) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByAuthId(authId.get());
        if (optionalUserProfile.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        optionalUserProfile.get().setStatus(EStatus.DELETED);
        update(optionalUserProfile.get());
        return true;
    }

    /**
     * findByUsername metodunu büyük küçük harf ayrımı yapmayacak şekilde yeniden düzenleyelim. bir username için birden fazla cache oluşmasının önüne geçelim.
     * yeni bir kullanıcı oluşturalım. sonra bu kullanıcıyı dün cachelediğimiz findbyusername metodu yardımıyla bulalım. Cache'lendiğinden emin olalım.
     * Sonra user-profile/update metodunu çalıştıralım ve bilgilerini güncelleyelim ve aynı user'ı yeniden findbyusername metodu ile bulalım.
     * Bir sorun ile karşılaşacaksınız. Bu sorunu tespit edelim. Nedenini anlayalım ve açıklayalım. Sonrasında bu soruna bir çözüm üretelim.
     */

    @Cacheable(value = "user-find-by-username", key = "#userName.toLowerCase()")
    public UserProfile findByUserName(String userName) { //userprofile dönüyo auth'a serilizable yazdık base entitye daha doğrusu
        try {
            Thread.sleep(2000L);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByUsernameIgnoreCase(userName);
        if (optionalUserProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        return optionalUserProfile.get();
    }

    /**
     * user-service'de findbyrole diye bir metot yazalım. bu metot girdiğimiz role göre bize db'deki userProfile'ları dönsün.
     * Bu metodu da cacheleyelim. Cache'imiz ne zaman silinmeli? -> Tespitini yapalım ve gerekli kod eklemelerini gerçekleştirelim.
     * Yeni bir kayıt olduğunda ve roel güncellendiğinde cache silinmeli çünkü listenin güncellenmesi gerekecek.
     */
    @Cacheable(value = "user-find-by-role", key = "#role.toUpperCase()") //USER //findbyrole::USER
    public List<UserProfile> findByRole(String role) {
       List<Long> authIdList= authManager.findByRole(role).getBody(); //Responseentity old. için list oalrak getbody ile yazabiliriz.

       return authIdList.stream().map( x-> userProfileRepository.findByAuthId(x)
               .orElseThrow(() -> new UserManagerException(ErrorType.USER_NOT_FOUND))).collect(Collectors.toList());
    }



    public UserResponseDto findByToken(String token) {
        Optional<Long> authId = jwtTokenManager.getIdFromToken(token);
        if (authId.isPresent()){
            UserProfile userProfile = userProfileRepository.findByAuthId(authId.get()).get();
            return UserProfileMapper.INSTANCE.fromUserProfileToUserProfileResponse(userProfile);
        }
        throw new UserManagerException(ErrorType.USER_NOT_FOUND);
    }

    public Optional<UserProfile> findByAuthId(Long id) {
        return userProfileRepository.findByAuthId(id);
    }


}
