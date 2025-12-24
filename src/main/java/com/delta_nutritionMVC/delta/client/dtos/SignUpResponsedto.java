package com.delta_nutritionMVC.delta.client.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponsedto {
    String fullName;
    String email;
    String phone;
    String addressLiv;
    String cityName;
}
