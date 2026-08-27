package com.hairhub.backend.mapper;

import com.hairhub.backend.dto.ServiceTypeCreateDTO;
import com.hairhub.backend.dto.ServiceTypeResponseDTO;
import com.hairhub.backend.entity.ServiceType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceTypeMapperTest {
    private final ServiceTypeMapper mapper = new ServiceTypeMapper();

    @Test
    void toServiceType_mapAllFieldsCorrectly() {
        ServiceTypeCreateDTO dto = new ServiceTypeCreateDTO("Tuns", 30);
        ServiceType result = mapper.toServiceType(dto);

        assertNull(result.getId());
        assertEquals("Tuns", result.getName());
        assertEquals(30, result.getDuration());
    }

    @Test
    void toServiceTypeDTO_mapAllFieldsCorrectly() {
        ServiceType sType = mock(ServiceType.class);
        when(sType.getId()).thenReturn(1L);
        when(sType.getName()).thenReturn("Tuns");
        when(sType.getDuration()).thenReturn(30);
        ServiceTypeResponseDTO dto = mapper.toServiceTypeDTO(sType);

        assertEquals(1L, dto.id());
        assertEquals("Tuns", dto.name());
        assertEquals(30, dto.duration());
    }
}
