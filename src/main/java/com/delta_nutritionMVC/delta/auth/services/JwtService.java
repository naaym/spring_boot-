package com.delta_nutritionMVC.delta.auth.services;

import com.delta_nutritionMVC.delta.user.models.UserEntity;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    public String generateToken(UserEntity user);
    public String extractEmail(String token);
    public String extractRole(String token);
    public Claims extractAllClaims(String token);
    public Boolean isTokenExpired(String token);
    public boolean validateToken(String token, UserDetails user);
}
