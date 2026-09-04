package com.hairhub.backend.repository.jpa;

import com.hairhub.backend.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    Optional<ServiceType> findByName(String name);

    List<ServiceType> findByPrice(Integer price);

    void deleteByName(String name);
}
