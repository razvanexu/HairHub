package com.hairhub.backend.repository.jpa;

import com.hairhub.backend.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataServiceTypeRepo extends JpaRepository<ServiceType, Long> {
    Optional<ServiceType> findByName(String name);
    void deleteByName(String name);
}
