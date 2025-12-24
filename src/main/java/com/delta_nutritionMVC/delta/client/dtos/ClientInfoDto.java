package com.delta_nutritionMVC.delta.client.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientInfoDto{
    String email ;
    Long clientId;
    String phone;
    String cityName;
    String addressLiv;
    boolean active;
    String fullName;
}