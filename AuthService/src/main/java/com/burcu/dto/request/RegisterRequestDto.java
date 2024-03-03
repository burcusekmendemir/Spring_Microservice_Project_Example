package com.burcu.dto.request;

import com.burcu.entity.BaseEntity;
import com.burcu.utility.enums.ERole;
import com.burcu.utility.enums.EStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequestDto {
    @Size(min=3, max=20, message = "Username must be between 3 and 20 characters")
    @NotNull
    private String username;

    @Email
    @NotNull
    private String email;

    @Size(min=8, max=32, message = "Password must be between 3 and 32 characters")
    @NotNull
    private String password;


}
