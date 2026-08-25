package com.hairhub.backend.controller;

import com.hairhub.backend.dto.ServiceTypeDTO;
import com.hairhub.backend.entity.ServiceType;
import com.hairhub.backend.mapper.ServiceTypeMapper;
import com.hairhub.backend.service.ServiceTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-types")
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;
    private final ServiceTypeMapper serviceTypeMapper;

    public ServiceTypeController(ServiceTypeService serviceTypeService, ServiceTypeMapper serviceTypeMapper) {
        this.serviceTypeService = serviceTypeService;
        this.serviceTypeMapper = serviceTypeMapper;
    }

    @GetMapping
    public ResponseEntity<List<ServiceTypeDTO>> getServiceTypes() {
        List<ServiceTypeDTO> sTypeList = serviceTypeService.findAll()
                .stream()
                .map(serviceTypeMapper::toServiceTypeDTO)
                .toList();
        return ResponseEntity.ok(sTypeList);
    }

    @PostMapping
    public ResponseEntity<ServiceTypeDTO> postServiceType(@Valid @RequestBody ServiceTypeDTO serviceTypeDTO) {
        ServiceType serviceType = serviceTypeMapper.toServiceType(serviceTypeDTO);
        ServiceType saved = serviceTypeService.create(serviceType);
        return ResponseEntity.status(201).body(serviceTypeMapper.toServiceTypeDTO(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceTypeDTO> getServiceTypeById(@PathVariable Long id) {
        ServiceType service = serviceTypeService.findById(id);
        ServiceTypeDTO sTypeDTO = serviceTypeMapper.toServiceTypeDTO(service);
        return ResponseEntity.ok(sTypeDTO);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ServiceTypeDTO> getServiceTypeByName(@PathVariable String name) {
        ServiceType serviceType = serviceTypeService.findByName(name);
        ServiceTypeDTO sTypeDTO = serviceTypeMapper.toServiceTypeDTO(serviceType);
        return ResponseEntity.ok(sTypeDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceTypeDTO> updateServiceType(@Valid @RequestBody ServiceTypeDTO serviceTypeDTO, @PathVariable Long id) {
        ServiceType existing = serviceTypeService.findById(id);
        existing.setName(serviceTypeDTO.name());
        existing.setDuration(serviceTypeDTO.duration());
        ServiceType updated = serviceTypeService.update(existing);
        return ResponseEntity.ok(serviceTypeMapper.toServiceTypeDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceType(@PathVariable Long id) {
        serviceTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteServiceTypeByName(@PathVariable String name) {
        serviceTypeService.deleteByName(name);
        return ResponseEntity.noContent().build();
    }
}
