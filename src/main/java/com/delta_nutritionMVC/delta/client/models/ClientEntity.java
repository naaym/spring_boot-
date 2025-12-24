package com.delta_nutritionMVC.delta.client.models;


import com.delta_nutritionMVC.delta.user.models.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "clients")
public class ClientEntity extends UserEntity {
    private String phone;
    private String cityName;
    private String addressLiv;
    private Boolean active=true;

}