package com.delta_nutritionMVC.delta.client.services;

import com.delta_nutritionMVC.delta.client.dtos.ClientInfoDto;
import com.delta_nutritionMVC.delta.client.dtos.ClientSignUpRequest;
import com.delta_nutritionMVC.delta.client.dtos.ClientUpdateProfilRequest;
import com.delta_nutritionMVC.delta.client.dtos.SignUpResponsedto;

import java.util.List;

public interface ClientService {
    List<ClientInfoDto> getClients();
    ClientInfoDto getClientById(Long id);
    ClientInfoDto getClientByEmail(String email);
    SignUpResponsedto signUpClient(ClientSignUpRequest clientRequest);
    ClientInfoDto updateClientProfile(String email, ClientUpdateProfilRequest request);
}