package com.hairhub.backend.repository;

import com.hairhub.backend.entity.ClientCredentials;

import java.util.Optional;

public interface ClientCredentialsRepository {
    ClientCredentials save(ClientCredentials credentials);
    Optional<ClientCredentials> findByClientId(Long clientId);
    boolean existsByClientId(Long clientId);
}
