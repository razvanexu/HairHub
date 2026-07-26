package com.hairhub.backend.repository.jpa;

import com.hairhub.backend.entity.ServiceType;
import com.hairhub.backend.repository.ServiceTypeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaServiceTypeRepo implements ServiceTypeRepository {
    private final SpringDataServiceTypeRepo springDataServiceTypeRepo;

    public JpaServiceTypeRepo(SpringDataServiceTypeRepo springDataServiceTypeRepo){
        this.springDataServiceTypeRepo = springDataServiceTypeRepo;
    }


    @Override
    public ServiceType save(ServiceType serviceType) {
        return springDataServiceTypeRepo.save(serviceType);
    }

    @Override
    public Optional<ServiceType> findById(Long id) {
        return springDataServiceTypeRepo.findById(id);
    }

    @Override
    public Optional<ServiceType> findByName(String name) {
        return springDataServiceTypeRepo.findByName(name);
    }

    @Override
    public List<ServiceType> findAll() {
        return springDataServiceTypeRepo.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return springDataServiceTypeRepo.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        springDataServiceTypeRepo.deleteById(id);
    }

    @Override
    public void deleteByName(String name) {
        springDataServiceTypeRepo.deleteByName(name);
    }
}
