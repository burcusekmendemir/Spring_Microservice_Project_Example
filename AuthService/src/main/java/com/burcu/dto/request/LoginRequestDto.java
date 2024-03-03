package com.burcu.dto.request;


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

public class LoginRequestDto {

    @Size(min=3, max=32)
    @NotNull
    String username;
    @Size(min=3, max=32)
    @NotNull
    String password;


}
