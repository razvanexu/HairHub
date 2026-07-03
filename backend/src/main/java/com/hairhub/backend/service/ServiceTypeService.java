package com.hairhub.backend.service;

import com.hairhub.backend.entity.ServiceType;
import com.hairhub.backend.exceptions.EntityNotFoundException;
import com.hairhub.backend.repository.ServiceTypeRepository;
import com.hairhub.backend.service.validators.DurationValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class ServiceTypeService {
    private final ServiceTypeRepository sRepo;
    private final DurationValidation duration;

    public ServiceTypeService(ServiceTypeRepository sRepo, DurationValidation duration) {
        this.sRepo = sRepo;
        this.duration = duration;
    }

    public ServiceType create(ServiceType sType) {
        duration.validate(sType.getDuration());
        ServiceType saved =  sRepo.save(sType);
        log.info("Service type created with id: {}", saved.getId());
        return saved;
    }

    public ServiceType update(ServiceType sType) {
        duration.validate(sType.getDuration());
        ServiceType saved =  sRepo.save(sType);
        log.info("Service type updated with id: {}", saved.getId());
        return saved;
    }

    public ServiceType findById(Long id) {
        return sRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Service type not found with id: {}", id);
                    return new EntityNotFoundException("Service type with id " + id + " not found");
        });
    }

    public ServiceType findByName(String name) {
        return sRepo.findByName(name)
                .orElseThrow(() -> {
                    log.warn("Service type not found with name: {}", name);
                    return new EntityNotFoundException("Service type " + name + " not found");
                });
    }

    public List<ServiceType> findAll() {
        log.debug("Fetching all service types");
        return sRepo.findAll();
    }

    public void deleteById(Long id) {
        if(!sRepo.existsById(id)) {
            log.warn("Attempted to delete non-existent service type with id: {}", id);
            throw new EntityNotFoundException("Service type with id " + id+" not found");
        }
        sRepo.deleteById(id);
        log.info("Service type deleted: {}", id);
    }

    public void deleteByName(String name) {
        if(!sRepo.findByName(name).isPresent()){
            log.warn("Attempted to delete non-existent service type with name: {}", name);
            throw new EntityNotFoundException("Service type "+ name +" not found");
        }
        sRepo.deleteByName(name);
        log.info("Service type deleted: {}", name);
    }
}
