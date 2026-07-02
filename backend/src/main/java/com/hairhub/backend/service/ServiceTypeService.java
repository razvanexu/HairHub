package com.hairhub.backend.service;

import com.hairhub.backend.entity.ServiceType;
import com.hairhub.backend.repository.ServiceTypeRepository;
import com.hairhub.backend.service.validators.DurationValidation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceTypeService {
    private final ServiceTypeRepository sRepo;
    private final DurationValidation duration;

    public ServiceTypeService(ServiceTypeRepository sRepo, DurationValidation duration) {
        this.sRepo = sRepo;
        this.duration = duration;
    }

    public ServiceType create(ServiceType sType) {
        duration.validate(sType.getDuration());
        return sRepo.save(sType);
    }

    public ServiceType update(ServiceType sType) {
        duration.validate(sType.getDuration());
        return sRepo.save(sType);
    }

    public ServiceType findById(Long id) {
        return sRepo.findById(id).orElseThrow(() -> new RuntimeException("Service type not found"));
    }

    public ServiceType findByName(String name) {
        return sRepo.findByName(name).orElseThrow(() -> new RuntimeException("Service type not found"));
    }

    public List<ServiceType> findAll() {
        return sRepo.findAll();
    }

    public void deleteById(Long id) {
        if(!sRepo.existsById(id)) {
            throw new RuntimeException("Service type not found");
        }
        sRepo.deleteById(id);
    }
}
