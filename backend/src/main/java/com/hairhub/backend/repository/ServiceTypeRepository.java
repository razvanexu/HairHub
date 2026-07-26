package com.hairhub.backend.repository;

import com.hairhub.backend.entity.ServiceType;

import java.util.List;
import java.util.Optional;

public interface ServiceTypeRepository{
    ServiceType save (ServiceType serviceType);
    Optional<ServiceType> findById(Long id);
    Optional<ServiceType> findByName(String name);
    List<ServiceType> findAll();
    boolean existsById(Long id);
    void deleteById(Long id);
    void deleteByName(String name);
}
