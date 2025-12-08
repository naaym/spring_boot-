package com.delta_nutritionMVC.delta.client.repositories;


import com.delta_nutritionMVC.delta.client.models.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByEmail(String email);
    boolean existsByEmail(String email);


}
