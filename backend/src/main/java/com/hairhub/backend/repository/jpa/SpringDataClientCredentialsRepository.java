package com.hairhub.backend.repository.jpa;

import com.hairhub.backend.entity.ClientCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpringDataClientCredentialsRepository extends JpaRepository<ClientCredentials, Long> {
    Optional<ClientCredentials> findByClientId(Long clientId);
    boolean existsByClientId(Long clientId);
}
