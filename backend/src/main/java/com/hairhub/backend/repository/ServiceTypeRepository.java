package com.hairhub.backend.repository;

import com.hairhub.backend.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    Optional<ServiceType> findByName(String name);
}
