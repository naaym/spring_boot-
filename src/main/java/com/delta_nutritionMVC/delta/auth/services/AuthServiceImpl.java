package com.delta_nutritionMVC.delta.auth.services;


import com.delta_nutritionMVC.delta.auth.dtos.SignInRequest;
import com.delta_nutritionMVC.delta.auth.dtos.SignInResponse;
import com.delta_nutritionMVC.delta.user.models.UserEntity;
import com.delta_nutritionMVC.delta.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager manager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public SignInResponse signIn(SignInRequest request) {



        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));


        String token = jwtService.generateToken(user);


        return new SignInResponse(
                "User Authenticated Successfully",
                token,
                user.getRole().name()
        );
    }
}

