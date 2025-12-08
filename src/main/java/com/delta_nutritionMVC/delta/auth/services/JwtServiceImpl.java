package com.delta_nutritionMVC.delta.auth.services;

import com.delta_nutritionMVC.delta.user.models.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret}")
    private  String SECRET ;
    @Override

    public String generateToken(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }
    @Override

    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    @Override
    public String extractRole(String token) {
        return extractAllClaims(token)
                .get("role").toString();
    }
    @Override

    public Claims extractAllClaims(String token) {
        return  Jwts.parser()
                .setSigningKey(SECRET).
                parseClaimsJws(token).getBody();
    }
    @Override

    public Boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }
    @Override

    public boolean validateToken(String token, UserDetails user) {
        try{

        return (extractAllClaims(token).getSubject().equals(user.getUsername()) && !isTokenExpired(token));
        }catch (Exception e){
            return false;
        }
    }
}
