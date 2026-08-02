package com.hairhub.backend.repository.jpa;

import com.hairhub.backend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    Optional<Client> findByPhone(String phone);
    List<Client> findByName(String name);
    List<Client> findByIsActiveIsTrue();
    List<Client> findByIsActiveIsFalse();
}
