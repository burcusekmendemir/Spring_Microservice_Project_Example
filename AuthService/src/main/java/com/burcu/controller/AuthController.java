package com.burcu.controller;

import com.burcu.dto.request.*;
import com.burcu.dto.response.RegisterResponseDto;
import com.burcu.entity.Auth;
import com.burcu.service.AuthService;
import com.burcu.utility.enums.ERole;
import com.burcu.utility.enums.JwtTokenManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static com.burcu.constants.RestApiUrls.*;

@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtTokenManager tokenManager;

    @PostMapping(REGISTER)
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(REGISTER_RABBITMQ)
    public ResponseEntity<RegisterResponseDto> registerWithRabbitMq(@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.registerWithRabbitMq(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> doLogin(@RequestBody @Valid LoginRequestDto dto){
        return ResponseEntity.ok(authService.doLogin(dto));
    }


    @PostMapping(ACTIVATE_STATUS)
    public ResponseEntity<Boolean> activateStatus(@RequestBody ActivateStatusRequestDto dto){
        return ResponseEntity.ok(authService.activateStatus(dto));
    }

    @PutMapping(UPDATE_EMAIL)
    public ResponseEntity<Boolean> updateEmail(@RequestBody UpdateEmailRequestDto dto){
        return ResponseEntity.ok(authService.updateEmail(dto));
    }

    @DeleteMapping(DELETE_BY_TOKEN)
    public ResponseEntity<Boolean> deleteByToken(@RequestParam String token){
        return ResponseEntity.ok(authService.softDeleteByToken(token));
    }

    @GetMapping("/create-token")
    public ResponseEntity<String> createToken(Long id, ERole role){
        return ResponseEntity.ok(tokenManager.createToken(id, role).get());
    }

    @GetMapping("/create-token2")
    public ResponseEntity<String> createToken(Long id){
        return ResponseEntity.ok(tokenManager.createToken(id).get());
    }

    @GetMapping("/get-id-from-token")
    public ResponseEntity<Long> getIdFromToken(String token){
        return ResponseEntity.ok(tokenManager.getIdFromToken(token).get());
    }
    @GetMapping("/get-role-from-token")
    public ResponseEntity<String> getRoleFromToken(String token){
        return ResponseEntity.ok(tokenManager.getRoleFromToken(token).get());
    }

    @GetMapping("/get-redis")
    public ResponseEntity<String> getRedis(String redisExample){
        return ResponseEntity.ok(authService.getRedis(redisExample));
    }

    @DeleteMapping("/redis-delete")
    public ResponseEntity<Void> redisDelete(){
        authService.redisDelete();
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/redis-delete-2")
    public ResponseEntity<Void> redisDelete2(String redisExample){
        authService.redisDelete2(redisExample);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find-by-role")
    public ResponseEntity<List<Long>> findByRole(@RequestParam String role){
        return ResponseEntity.ok(authService.findByRole(role));
    }




}
