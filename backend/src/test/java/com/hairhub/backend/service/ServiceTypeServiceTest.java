package com.hairhub.backend.service;

import com.hairhub.backend.entity.ServiceType;
import com.hairhub.backend.exceptions.EntityNotFoundException;
import com.hairhub.backend.repository.ServiceTypeRepository;
import com.hairhub.backend.service.validators.DurationValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTypeServiceTest {

    @Mock
    private ServiceTypeRepository repository;

    @Mock
    private DurationValidation durationValidation;

    private ServiceTypeService service;

    @BeforeEach
    void setUp() {
        service = new ServiceTypeService(repository, durationValidation);
    }

    @Test
    void create_withValidServiceType_savesAndReturns() {
        //Given
        ServiceType input = new ServiceType("Tuns", 50, 30, null);
        when(repository.save(input)).thenReturn(input);

        //When
        ServiceType result = service.create(input);

        //Then
        assertEquals("Tuns", result.getName());
        assertEquals(50, result.getPrice());
        assertEquals(30, result.getDuration());
        assertNull(result.getDescription());
        verify(repository).save(input);
    }

    @Test
    void findById_withExistingId_returnsServiceType() {
        //Given
        Long id = 1L;
        ServiceType input = mock(ServiceType.class);
        when(input.getId()).thenReturn(1L);
        when(input.getName()).thenReturn("Tuns");
        when(input.getDuration()).thenReturn(30);
        when(repository.findById(id)).thenReturn(Optional.of(input));

        //When
        ServiceType result = service.findById(id);

        //Then
        assertEquals(id, result.getId());
        assertEquals("Tuns", result.getName());
        assertEquals(30, result.getDuration());
    }

    @Test
    void findById_withNonExistentId_throwsEntityNotFoundException() {
        //Given
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        //When
        //Then
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void findByName_withValidServiceType_returnsServiceType() {
        //Given
        ServiceType input = mock(ServiceType.class);
        when(input.getId()).thenReturn(1L);
        when(input.getName()).thenReturn("Tuns");
        when(input.getDuration()).thenReturn(30);

        when(repository.findByName(input.getName())).thenReturn(Optional.of(input));

        //When
        ServiceType result = service.findByName(input.getName());

        //Then
        assertEquals(1L, result.getId());
        assertEquals("Tuns", result.getName());
        assertEquals(30, result.getDuration());
    }

    @Test
    void findByName_withNonExistentName_throwsEntityNotFoundException() {
        //Given
        String name = "Smaranda";
        when(repository.findByName(name)).thenReturn(Optional.empty());

        //When
        //Then
        assertThrows(EntityNotFoundException.class, () -> service.findByName(name));
    }

    @Test
    void findAll_withValidServiceType_returnsAllServiceTypes() {
        //Given
        ServiceType s1 = new ServiceType("Tuns", 50, 30, null);
        ServiceType s2 = new ServiceType("Coafor", 100, 60, null);
        when(repository.findAll()).thenReturn(List.of(s1, s2));

        //When
        List<ServiceType> result = service.findAll();

        //Then
        assertEquals(2, result.size());
        assertEquals(s1, result.get(0));
        assertEquals(s2, result.get(1));
        verify(repository).findAll();
    }

    @Test
    void deleteById_withValidId_deletesServiceTypeById() {
        //Given
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        //When
        service.deleteById(id);

        //Then
        verify(repository).deleteById(id);
    }

    @Test
    void deleteById_withNonExistentId_throwsEntityNotFoundException() {
        //Given
        Long id = 999L;
        when(repository.existsById(id)).thenReturn(false);

        //When

        //Then
        assertThrows(EntityNotFoundException.class, () -> service.deleteById(id));
        verify(repository, never()).deleteById(id);
    }
}
