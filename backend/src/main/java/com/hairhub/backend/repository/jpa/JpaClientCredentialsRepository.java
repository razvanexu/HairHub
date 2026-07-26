package com.hairhub.backend.repository.jpa;

import com.hairhub.backend.entity.ClientCredentials;
import com.hairhub.backend.repository.ClientCredentialsRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaClientCredentialsRepository implements ClientCredentialsRepository {
    private final SpringDataClientCredentialsRepository springDataClientCredentialsRepository;

    public JpaClientCredentialsRepository(SpringDataClientCredentialsRepository springDataClientCredentialsRepository){
        this.springDataClientCredentialsRepository = springDataClientCredentialsRepository;
    }

    @Override
    public ClientCredentials save(ClientCredentials credentials){
        return springDataClientCredentialsRepository.save(credentials);
    }

    @Override
    public Optional<ClientCredentials> findByClientId(Long clientId) {
        return springDataClientCredentialsRepository.findByClientId(clientId);
    }

    @Override
    public boolean existsByClientId(Long clientId) {
        return springDataClientCredentialsRepository.existsByClientId(clientId);
    }
}
