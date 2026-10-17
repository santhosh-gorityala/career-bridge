package com.jobPortal.careerBridge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {

    @NotBlank(message = "Email Cannot Be Blank or Null")
    @Email
    private String email;

    @NotBlank(message = "Password Cannot Be Blank or Empty")
    @Size(min = 8,max = 100,message = "Password must be min 8 char length")
    private String password;

    @NotBlank(message = "firstName Cannot Be Blank")
    @Size(min=3,max=100,message = "firstName must be min 3 char length")
    private String firstName;

    @NotBlank(message = "lastName Cannot be Blank")
    @Size(max = 100)
    private String lastName;

    @NotBlank(message = "phone number cannot be blank")
    @Size(max = 20)
    private String phone;

}
