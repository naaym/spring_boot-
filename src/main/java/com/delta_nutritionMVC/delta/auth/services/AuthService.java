package com.delta_nutritionMVC.delta.auth.services;

import com.delta_nutritionMVC.delta.auth.dtos.SignInRequest;
import com.delta_nutritionMVC.delta.auth.dtos.SignInResponse;

public interface AuthService {
    SignInResponse signIn(SignInRequest signInRequest);
}
