package com.jobPortal.careerBridge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "email cannot be blank")
    @Email
    private String email;

    @NotBlank(message = "password cannot be blank")
    private String password;


}
