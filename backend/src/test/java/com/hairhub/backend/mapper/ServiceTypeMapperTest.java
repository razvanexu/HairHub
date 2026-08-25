package com.hairhub.backend.mapper;

import com.hairhub.backend.dto.ServiceTypeDTO;
import com.hairhub.backend.entity.ServiceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceTypeMapperTest {
    private final ServiceTypeMapper mapper = new ServiceTypeMapper();

    @Test
    void toServiceType_mapAllFieldsCorrectly() {
        ServiceTypeDTO dto = new ServiceTypeDTO(1L, "Tuns", 30);
        ServiceType result = mapper.toServiceType(dto);

        assertEquals(1L, result.getId());
        assertEquals("Tuns", result.getName());
        assertEquals(30, result.getDuration());
    }

    @Test
    void toServiceTypeDTO_mapAllFieldsCorrectly() {
        ServiceType sType = new ServiceType("Tuns", 30);
        sType.setId(1L);
        ServiceTypeDTO dto = mapper.toServiceTypeDTO(sType);

        assertEquals(1L, dto.id());
        assertEquals("Tuns", dto.name());
        assertEquals(30, dto.duration());
    }
}
