package com.delta_nutritionMVC.delta.client.services;

import com.delta_nutritionMVC.delta.client.dtos.ClientInfoDto;
import com.delta_nutritionMVC.delta.client.dtos.ClientSignUpRequest;
import com.delta_nutritionMVC.delta.client.dtos.ClientUpdateProfilRequest;
import com.delta_nutritionMVC.delta.client.dtos.SignUpResponsedto;
import com.delta_nutritionMVC.delta.client.models.ClientEntity;
import com.delta_nutritionMVC.delta.client.repositories.ClientRepository;
import com.delta_nutritionMVC.delta.user.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<ClientInfoDto> getClients() {
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientInfoDto(
                        client.getEmail(),
                        client.getUserId(),
                        client.getPhone(),
                        client.getCityName(),
                        client.getAddressLiv(),
                        client.getActive(),
                        client.getFullName(),
                        client.getAge()
                ))
                .toList();
    }

    @Override
    public SignUpResponsedto signUpClient(ClientSignUpRequest clientRequest) {

        if (clientRepository.existsByEmail(clientRequest.getEmail())) {
            throw new RuntimeException("Email already Exists");
        }

        ClientEntity entity = new ClientEntity();

        // Remplir l’entité depuis le DTO REST
        entity.setFullName(clientRequest.getFullName());
        entity.setEmail(clientRequest.getEmail());
        entity.setPassword(passwordEncoder.encode(clientRequest.getPassword()));
        entity.setPhone(clientRequest.getPhone());
        entity.setAddressLiv(clientRequest.getAddressLiv());
        entity.setCityName(clientRequest.getCityName());
        entity.setAge(clientRequest.getAge());
        entity.setRole(Role.CLIENT);
        entity.setActive(true);

        clientRepository.save(entity);

        return new SignUpResponsedto(entity.getFullName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAddressLiv(),
                entity.getCityName(),
                entity.getAge());
    }

    @Override
    public ClientInfoDto getClientById(Long id) {
        return clientRepository.findById(id)
                .map(client -> new ClientInfoDto(
                        client.getEmail(),
                        client.getUserId(),
                        client.getPhone(),
                        client.getCityName(),
                        client.getAddressLiv(),
                        client.getActive(),
                        client.getFullName(),
                        client.getAge()
                ))
                .orElse(null);
    }

    @Override
    public ClientInfoDto getClientByEmail(String email) {
        return clientRepository.findByEmail(email)
                .map(client -> new ClientInfoDto(
                        client.getEmail(),
                        client.getUserId(),
                        client.getPhone(),
                        client.getCityName(),
                        client.getAddressLiv(),
                        client.getActive(),
                        client.getFullName(),
                        client.getAge()
                ))
                .orElse(null);
    }

    @Override
    public ClientInfoDto updateClientProfile(String email, ClientUpdateProfilRequest request) {
        ClientEntity client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        client.setFullName(request.getFullName());
        client.setPhone(request.getPhone());
        client.setAddressLiv(request.getAddressLiv());
        client.setCityName(request.getCityName());

        ClientEntity updated = clientRepository.save(client);

        return new ClientInfoDto(
                updated.getEmail(),
                updated.getUserId(),
                updated.getPhone(),
                updated.getCityName(),
                updated.getAddressLiv(),
                updated.getActive(),
                updated.getFullName(),
                updated.getAge()
        );
    }
}
