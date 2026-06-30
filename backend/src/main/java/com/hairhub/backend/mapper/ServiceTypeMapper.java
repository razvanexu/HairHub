package com.hairhub.backend.mapper;

import com.hairhub.backend.dto.ServiceTypeDTO;
import com.hairhub.backend.entity.ServiceType;
import org.springframework.stereotype.Component;

@Component
public class ServiceTypeMapper {
    public ServiceType toServiceType(ServiceTypeDTO sTypeDTO) {
        ServiceType sType = new ServiceType();
        sType.setId(sTypeDTO.id());
        sType.setName(sTypeDTO.name());
        sType.setDuration(sTypeDTO.duration());
        return sType;
    }

    public  ServiceTypeDTO toServiceTypeDTO(ServiceType sType) {
        return new ServiceTypeDTO(sType.getId(), sType.getName(), sType.getDuration());
    }
}
