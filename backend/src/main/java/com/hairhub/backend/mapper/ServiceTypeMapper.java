package com.hairhub.backend.mapper;

import com.hairhub.backend.dto.ServiceTypeCreateDTO;
import com.hairhub.backend.dto.ServiceTypeResponseDTO;
import com.hairhub.backend.entity.ServiceType;
import org.springframework.stereotype.Component;

@Component
public class ServiceTypeMapper {
    public ServiceType toServiceType(ServiceTypeCreateDTO sTypeDTO) {
        return new ServiceType(sTypeDTO.name(), sTypeDTO.duration());
    }

    public ServiceTypeResponseDTO toServiceTypeDTO(ServiceType sType) {
        return new ServiceTypeResponseDTO(sType.getId(), sType.getName(), sType.getDuration());
    }

    public void updateServiceType(ServiceType existing, ServiceTypeCreateDTO dto) {
        existing.setName(dto.name());
        existing.setDuration(dto.duration());
    }
}
