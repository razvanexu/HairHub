package com.hairhub.backend.controller;

import com.hairhub.backend.dto.ServiceTypeCreateDTO;
import com.hairhub.backend.dto.ServiceTypeResponseDTO;
import com.hairhub.backend.entity.ServiceType;
import com.hairhub.backend.mapper.ServiceTypeMapper;
import com.hairhub.backend.service.ServiceTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-type")
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;
    private final ServiceTypeMapper serviceTypeMapper;

    public ServiceTypeController(ServiceTypeService serviceTypeService, ServiceTypeMapper serviceTypeMapper) {
        this.serviceTypeService = serviceTypeService;
        this.serviceTypeMapper = serviceTypeMapper;
    }

    @GetMapping
    public ResponseEntity<List<ServiceTypeResponseDTO>> getServiceTypes() {
        List<ServiceTypeResponseDTO> sTypeList = serviceTypeService.findAll()
                .stream()
                .map(serviceTypeMapper::toServiceTypeDTO)
                .toList();
        return ResponseEntity.ok(sTypeList);
    }

    @PostMapping
    public ResponseEntity<ServiceTypeResponseDTO> postServiceType(@Valid @RequestBody ServiceTypeCreateDTO serviceTypeDTO) {
        ServiceType serviceType = serviceTypeMapper.toServiceType(serviceTypeDTO);
        ServiceType saved = serviceTypeService.create(serviceType);
        return ResponseEntity.status(201).body(serviceTypeMapper.toServiceTypeDTO(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceTypeResponseDTO> getServiceTypeById(@PathVariable Long id) {
        ServiceType service = serviceTypeService.findById(id);
        ServiceTypeResponseDTO sTypeDTO = serviceTypeMapper.toServiceTypeDTO(service);
        return ResponseEntity.ok(sTypeDTO);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ServiceTypeResponseDTO> getServiceTypeByName(@PathVariable String name) {
        ServiceType serviceType = serviceTypeService.findByName(name);
        ServiceTypeResponseDTO sTypeDTO = serviceTypeMapper.toServiceTypeDTO(serviceType);
        return ResponseEntity.ok(sTypeDTO);
    }

    @GetMapping("/price")
    public ResponseEntity<List<ServiceTypeResponseDTO>> getServiceByPrice(@PathVariable Integer price) {
        List<ServiceTypeResponseDTO> foundServices = serviceTypeService.findByPrice(price)
                .stream().map(serviceTypeMapper::toServiceTypeDTO).toList();
        return ResponseEntity.ok(foundServices);
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

    @PutMapping("{id}")
    public ResponseEntity<ServiceTypeResponseDTO> updateServiceType(
            @Valid @RequestBody ServiceTypeCreateDTO dto, @PathVariable Long id) {
        ServiceType existing = serviceTypeService.findById(id);
        serviceTypeMapper.updateServiceType(existing, dto);
        ServiceType updated = serviceTypeService.update(existing);
        return ResponseEntity.ok(serviceTypeMapper.toServiceTypeDTO(updated));
    }
}
