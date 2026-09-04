package com.hairhub.backend.repository.jpa;

import com.hairhub.backend.entity.ServiceType;
import com.hairhub.backend.repository.ServiceTypeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaServiceTypeRepo implements ServiceTypeRepository {
    private final SpringDataServiceTypeRepository springDataServiceTypeRepository;

    public JpaServiceTypeRepo(SpringDataServiceTypeRepository springDataServiceTypeRepository) {
        this.springDataServiceTypeRepository = springDataServiceTypeRepository;
    }

    @Override
    public ServiceType save(ServiceType serviceType) {
        return springDataServiceTypeRepository.save(serviceType);
    }

    @Override
    public Optional<ServiceType> findById(Long id) {
        return springDataServiceTypeRepository.findById(id);
    }

    @Override
    public Optional<ServiceType> findByName(String name) {
        return springDataServiceTypeRepository.findByName(name);
    }

    @Override
    public List<ServiceType> findByPrice(Integer price) {
        return springDataServiceTypeRepository.findByPrice(price);
    }

    @Override
    public List<ServiceType> findAll() {
        return springDataServiceTypeRepository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return springDataServiceTypeRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        springDataServiceTypeRepository.deleteById(id);
    }

    @Override
    public void deleteByName(String name) {
        springDataServiceTypeRepository.deleteByName(name);
    }
}
