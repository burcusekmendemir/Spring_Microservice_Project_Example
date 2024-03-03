package com.burcu.service;

import com.burcu.dto.request.*;
import com.burcu.dto.response.RegisterResponseDto;
import com.burcu.entity.Auth;
import com.burcu.exception.AuthServiceException;
import com.burcu.exception.ErrorType;
import com.burcu.manager.UserManager;
import com.burcu.mapper.RegisterMapper;
import com.burcu.rabbitmq.model.ElasticModel;
import com.burcu.rabbitmq.model.RegisterModel;
import com.burcu.rabbitmq.model.RegisterMailModel;
import com.burcu.rabbitmq.producer.ElasticProducer;
import com.burcu.rabbitmq.producer.RegisterProducer;
import com.burcu.rabbitmq.producer.RegisterMailProducer;
import com.burcu.repository.AuthRepository;
import com.burcu.utility.enums.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
//@RequiredArgsConstructor //extendten geleni tanımaz, superclass ile çalışmaz
public class AuthService extends ServiceManager<Auth,Long> {

    private final AuthRepository authRepository;
    private final UserManager userManager;
    private final JwtTokenManager jwtTokenManager;
    private final CacheManager cacheManager;
    private final RegisterProducer registerProducer;
    private final RegisterMailProducer registerMailProducer;
    private final ElasticProducer elasticProducer;

    public AuthService(AuthRepository authRepository, UserManager userManager, JwtTokenManager jwtTokenManager, CacheManager cacheManager, RegisterProducer registerProducer, RegisterMailProducer registerMailProducer, ElasticProducer elasticProducer) {
        super(authRepository);
        this.authRepository = authRepository;
        this.userManager = userManager;
        this.jwtTokenManager = jwtTokenManager;
        this.cacheManager = cacheManager;
        this.registerProducer = registerProducer;
        this.registerMailProducer = registerMailProducer;
        this.elasticProducer = elasticProducer;
    }

    /**
     * @Transactional ->  Bu methodun sonuna gelip return atana kadar transctionalda (geçiş sürecinde)
     * tutuyo hatayla karşılaşmamışsa kalıcı hale getiriyor
     * @param dto
     * @return
     */
//    @Transactional
    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = RegisterMapper.INSTANCE.fromRegisterRequestDtoToAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        save(auth);
        userManager.createUser(RegisterMapper.INSTANCE.fromAuthToCreateUserRequestDto(auth));
        Objects.requireNonNull(cacheManager.getCache("user-find-by-role")).evict(auth.getRole().toString().toUpperCase());

        return RegisterMapper.INSTANCE.fromAuthToRegisterResponseDto(auth);
    }
    public RegisterResponseDto registerWithRabbitMq(RegisterRequestDto dto) {
        Auth auth = RegisterMapper.INSTANCE.fromRegisterRequestDtoToAuth(dto);
        auth.setActivationCode(CodeGenerator.generateCode());
        try{
            save(auth);

            registerProducer.sendNewUser(RegisterModel.builder()
                    .authId(auth.getId())
                    .username(auth.getUsername())
                    .email(auth.getEmail())
                    .build());
            registerMailProducer.sendActivationCode(RegisterMailModel.builder()
                    .email(auth.getEmail())
                    .username(auth.getUsername())
                    .activationCode(auth.getActivationCode())
                    .build());
            elasticProducer.sendMessage(ElasticModel.builder()
                    .email(auth.getEmail())
                    .authId(auth.getId())
                    .username(auth.getUsername())
                    .build());

            Objects.requireNonNull(cacheManager.getCache("user-find-by-role")).evict(auth.getRole().toString().toUpperCase());
        }catch (Exception e){

            throw new AuthServiceException(ErrorType.USER_NOT_CREATED);
        }

        return RegisterMapper.INSTANCE.fromAuthToRegisterResponseDto(auth);
    }

    public String doLogin(LoginRequestDto dto) {
        Optional<Auth> authOptional = authRepository.findOptionalByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if (authOptional.isEmpty()) {
            throw new AuthServiceException(ErrorType.LOGIN_ERROR);
        }
        if (authOptional.get().getStatus().equals(EStatus.ACTIVE)) {
            return jwtTokenManager.createToken(authOptional.get().getId(), authOptional.get().getRole())
                    .orElseThrow(() -> {
                        throw new AuthServiceException(ErrorType.TOKEN_NOT_CREATED); //metot olduğu için ; ile belirttik
                    });
        } else {
            throw new AuthServiceException(ErrorType.ACCOUNT_NOT_ACTIVE);
        }
    }

    /**
     * Kullanıcının Status'unu aktif hale getirmek için aktivasyon kod doğrulaması yapan bir metot yazınız.
     */

    public Boolean activateStatus(ActivateStatusRequestDto dto) {
        Optional<Auth> optionalAuth = findById(dto.getAuthId());
        if (optionalAuth.isEmpty()) {
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        if (optionalAuth.get().getActivationCode().equals(dto.getActivationCode())) {
            optionalAuth.get().setStatus(EStatus.ACTIVE);
            update(optionalAuth.get());
            userManager.activateStatus(optionalAuth.get().getId());
            return true;
        } else {
            throw new AuthServiceException(ErrorType.ACTIVATION_CODE_ERROR);
        }
    }

    public Boolean updateEmail(UpdateEmailRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findById(dto.getId());
        if (optionalAuth.isEmpty()) {
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        optionalAuth.get().setEmail(dto.getEmail());
        update(optionalAuth.get());
        return true;
    }

    //methodu overload ettiki geri dönüş tipi farklı

//    public Boolean softDeleteById(Long id){
//        Optional<Auth> optionalAuth = authRepository.findById(id);
//        if (optionalAuth.isEmpty()) {
//            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
//        }
//        optionalAuth.get().setStatus(EStatus.DELETED);
//        update(optionalAuth.get());
//        userManager.deleteById(id);
//        return true;
//    }

    public Boolean softDeleteByToken(String token){
        Optional<Long> optionalId = jwtTokenManager.getIdFromToken(token);
        if (optionalId.isEmpty()) {
            throw new AuthServiceException(ErrorType.INVALID_TOKEN);
        }
        Optional<Auth> optionalAuth = authRepository.findById(optionalId.get());
        if (optionalAuth.isEmpty()) {
            throw new AuthServiceException(ErrorType.USER_NOT_FOUND);
        }
        optionalAuth.get().setStatus(EStatus.DELETED);
        update(optionalAuth.get());
        userManager.deleteByToken(token);
        return true;
    }

    @Cacheable(value = "get-redis") //TODO uppercase ekle
    public String getRedis(String redisExample){
        String redis= redisExample.toUpperCase().concat(("***** REDİS TEST ****"));
        try {
            Thread.sleep(3000L); //aynıv eriyi tekrar gönderirsek belleğe önceden alındığı için sonrakinde thread uyumaz fazla sn de gönderir
        }catch (InterruptedException e){
            log.error("thread hatası");
        }
        return redis;
    }

    public void redisDelete(){
        Objects.requireNonNull(cacheManager.getCache("get-redis")).clear();
    }
    public void redisDelete2(String redisExample){
        Objects.requireNonNull(cacheManager.getCache("get-redis")).evict(redisExample);
    }


    public List<Long> findByRole(String role) {
        ERole myrole;
        try {
            myrole= ERole.valueOf(role.toUpperCase(Locale.ENGLISH)); //admin -> ADMİN -> ADMIN
        }catch (Exception e){
            throw new RuntimeException("Role not found...");
        }
        return authRepository.findAllByRole(myrole).stream().map(Auth::getId).collect(Collectors.toList());
    }


}