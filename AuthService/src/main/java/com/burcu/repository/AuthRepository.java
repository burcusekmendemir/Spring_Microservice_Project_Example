package com.burcu.repository;

import com.burcu.entity.Auth;
import com.burcu.utility.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth,Long> {
    Optional<Auth> findOptionalByUsernameAndPassword(String username, String password);

    Optional<Auth> findOptionalByActivationCode(String code);

    List<Auth> findAllByRole(ERole rol);
}
