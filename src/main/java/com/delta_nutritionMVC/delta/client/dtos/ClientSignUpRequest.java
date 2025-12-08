package com.delta_nutritionMVC.delta.client.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientSignUpRequest {
    @NotBlank
    String fullName;
    @Email
    @NotBlank(message = "Email required ! ")
    String email;

    String password;
    String phone;
    String addressLiv;
    String cityName;
    int age ;
}
