package com.hairhub.backend.repository;

import com.hairhub.backend.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Client save (Client client);
    Optional<Client> findById(Long id);
    Optional<Client> findByEmail(String email);
    Optional<Client> findByPhone(String phone);
    List<Client> findByName(String name);
    List<Client> findByIsActiveIsTrue();
    List<Client> findByIsActiveIsFalse();
    List<Client> findAll();
}
