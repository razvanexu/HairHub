package com.hairhub.backend.mapper;

import com.hairhub.backend.dto.ClientCreateDTO;
import com.hairhub.backend.dto.ClientResponseDTO;
import com.hairhub.backend.entity.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientMapperTest {
    private final ClientMapper clientMapper = new ClientMapper();

    @Test
    void toClient_mapsAllFieldsCorrectly() {
        //Given
        ClientCreateDTO createDTO = new ClientCreateDTO("Ion", "0745154254", "ion@test.ro");

        //When
        Client result = clientMapper.toClient(createDTO);

        //Then
        assertNull(result.getId());
        assertEquals("Ion", result.getName());
        assertEquals("0745154254", result.getPhone());
        assertEquals("ion@test.ro", result.getEmail());
        assertTrue(result.getIsActive());
    }

    @Test
    void toClientDTO_mapsAllFieldsCorrectly() {
        //Given
        Client client = mock(Client.class);
        when(client.getId()).thenReturn(1L);
        when(client.getName()).thenReturn("Ion");
        when(client.getPhone()).thenReturn("0745154254");
        when(client.getEmail()).thenReturn("ion@test.ro");
        when(client.getIsActive()).thenReturn(true);

        //When
        ClientResponseDTO result = clientMapper.toClientDTO(client);

        //Then
        assertEquals(1L, result.id());
        assertEquals("Ion", result.name());
        assertEquals("0745154254", result.phone());
        assertEquals("ion@test.ro", result.email());
        assertTrue(result.isActive());
    }

    @Test
    void updateClient_updatesWithNewDto() {
        //Given
        Client existing = new Client("Ion", "0547254123", "ion@test.ro");
        existing.setIsActive(true);
        ClientCreateDTO dto = new ClientCreateDTO("Vasile", "0745154368", "vasile@test.ro");

        //When
        clientMapper.updateClient(existing, dto);

        //Then
        assertEquals("Vasile", existing.getName());
        assertEquals("0745154368", existing.getPhone());
        assertEquals("vasile@test.ro", existing.getEmail());
        assertTrue(existing.getIsActive());
    }

    @Test
    void updateClient_updatesWithNew_andKeepsIsInactive() {
        //Given
        Client existing = new Client("Ion", "0547254123", "ion@test.ro");
        existing.setIsActive(false);
        ClientCreateDTO dto = new ClientCreateDTO("Vasile", "0745154368", "vasile@test.ro");

        //When
        clientMapper.updateClient(existing, dto);

        //Then
        assertEquals("Vasile", existing.getName());
        assertEquals("0745154368", existing.getPhone());
        assertEquals("vasile@test.ro", existing.getEmail());
        assertFalse(existing.getIsActive());
    }
}
