package com.hairhub.backend.mapper;

import com.hairhub.backend.dto.ServiceTypeCreateDTO;
import com.hairhub.backend.dto.ServiceTypeResponseDTO;
import com.hairhub.backend.entity.ServiceType;
import org.springframework.stereotype.Component;

@Component
public class ServiceTypeMapper {
    public ServiceType toServiceType(ServiceTypeCreateDTO sTypeDTO) {
        return new ServiceType(sTypeDTO.name(), sTypeDTO.price(), sTypeDTO.duration(), sTypeDTO.description());
    }

    public ServiceTypeResponseDTO toServiceTypeDTO(ServiceType sType) {
        return new ServiceTypeResponseDTO(
                sType.getId(), sType.getName(), sType.getPrice(), sType.getDuration(), sType.getDescription());
    }

    public void updateServiceType(ServiceType existing, ServiceTypeCreateDTO dto) {
        existing.setName(dto.name());
        existing.setPrice(dto.price());
        existing.setDuration(dto.duration());
        existing.setDescription(dto.description());
    }
}
