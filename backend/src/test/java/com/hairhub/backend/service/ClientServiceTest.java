package com.hairhub.backend.service;

import com.hairhub.backend.entity.Client;
import com.hairhub.backend.exceptions.EntityNotFoundException;
import com.hairhub.backend.repository.ClientRepository;
import com.hairhub.backend.service.validators.PhoneValidation;
import jakarta.validation.ValidationException;
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
class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PhoneValidation phoneValidation;

    private ClientService clientService;

    @BeforeEach
    void setUp() {
        clientService = new ClientService(clientRepository, phoneValidation);
    }

    @Test
    void create_withValidClient_savesAndReturnsClient(){
        //Given
        Client input = new Client("Ion", "0725475548", "ion@mail.de");
        when(clientRepository.save(input)).thenReturn(input);

        //When
        Client result = clientService.create(input);

        //Then
        assertEquals("Ion", result.getName());
        assertEquals("0725475548", result.getPhone());
        assertEquals("ion@mail.de", result.getEmail());
        verify(clientRepository).save(input);
    }

    @Test
    void create_withInvalidPhone_throwsException(){
        //Given
        Client input = new Client("Ion", "+40725475548", "ion@mail.de");
        doThrow(new ValidationException("Invalid Phone"))
        .when(phoneValidation).validate(input.getPhone());

        //When //Then
        assertThrows(ValidationException.class, ()->clientService.create(input));
        verify(clientRepository, never()).save(input);
    }

    @Test
    void update_withValidClient_savesAndReturns(){
        //Given
        Client input = new Client("Ion", "0725475548", "ion@mail.de");
        input.setName("Vasile");
        input.setPhone("0725475548");
        input.setEmail("Vasile@mail.de");
        when(clientRepository.save(input)).thenReturn(input);

        //When
        Client result = clientService.update(input);

        // Then
        assertEquals("Vasile", result.getName());
        assertEquals("0725475548", result.getPhone());
        assertEquals("Vasile@mail.de", result.getEmail());
    }

    @Test
    void update_withInvalidPhone_throwsException(){
        //Given
        Client input = new Client("Ion", "+40725475548", "ion@mail.de");
        doThrow(new ValidationException("Invalid Phone"))
                .when(phoneValidation).validate(input.getPhone());

        //When //Then
        assertThrows(ValidationException.class, ()->clientService.update(input));
        verify(clientRepository, never()).save(input);
    }

    @Test
    void findById_withExistentId_returnsClient(){
        //Given
        Long id = 1L;
        Client client = new Client("Ion", "0734654749", "ion@test.ro");
        client.setId(id);
        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        //When
        Client result = clientService.findById(id);

        //Then
        assertEquals(1L, result.getId());
        assertEquals("Ion", result.getName());
        assertEquals("0734654749", result.getPhone());
        assertEquals("ion@test.ro", result.getEmail());
    }

    @Test
    void findById_withNonExistentId_throwsEntityNotFoundException(){
        //Given
        Long id = 999L;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        //When

        //Then
        assertThrows(EntityNotFoundException.class, ()->clientService.findById(id));
    }

    @Test
    void findById_negativeId_throwsEntityNotFoundException(){
        //Given
        Long id = -1L;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        //When

        //Then
        assertThrows(EntityNotFoundException.class, ()->clientService.findById(id));
    }

    @Test
    void findByPhone_withExistentPhoneNumber_returnsClient(){
        //Given
        Long id = 1L;
        Client client = new Client("Ion", "0734654749", "ion@test.ro");
        client.setId(id);
        when(clientRepository.findByPhone(client.getPhone())).thenReturn(Optional.of(client));

        //When
        Client result = clientService.findByPhone(client.getPhone());

        //Then
        assertEquals(1L, result.getId());
        assertEquals("Ion", result.getName());
        assertEquals("0734654749", result.getPhone());
        assertEquals("ion@test.ro", result.getEmail());
    }

    @Test
    void findByPhone_withNonExistentPhoneNumber_throwsEntityNotFoundException(){
        //Given
        String missing = "0745678418";
        when(clientRepository.findByPhone(missing)).thenReturn(Optional.empty());

        //When

        //Then
        assertThrows(EntityNotFoundException.class, ()->clientService.findByPhone(missing));
    }

    @Test
    void findByEmail_withExistentEmail_returnsClient(){
        //Given
        Long id = 1L;
        Client client = new Client("Ion", "0734654749", "ion@test.ro");
        client.setId(id);
        when(clientRepository.findByEmail(client.getEmail())).thenReturn(Optional.of(client));

        //When
        Client result = clientService.findByEmail(client.getEmail());

        //Then
        assertEquals(1L, result.getId());
        assertEquals("Ion", result.getName());
        assertEquals("0734654749", result.getPhone());
        assertEquals("ion@test.ro", result.getEmail());
    }

    @Test
    void findByEmail_withNonExistentEmail_throwsEntityNotFoundException(){
        //Given
        String missing = "missing@de.ro";
        when(clientRepository.findByEmail(missing)).thenReturn(Optional.empty());

        //When

        //Then
        assertThrows(EntityNotFoundException.class, ()->clientService.findByEmail(missing));
    }

    @Test
    void findByName_withExistentName_returnsClientList(){
        //Given
        Long id = 1L;
        Client client = new Client("Ion", "0734654749", "ion@test.ro");
        client.setId(id);
        when(clientRepository.findByName(client.getName())).thenReturn(List.of(client));

        //When
        List<Client> result = clientService.findByName(client.getName());

        //Then
        assertEquals(1, result.size());
        assertEquals("Ion", result.get(0).getName());
    }

    @Test
    void findByName_withNonExistentName_returnsEmptyList(){
        //Given
        String missing = "missing";
        when(clientRepository.findByName(missing)).thenReturn(List.of());

        //When
        List<Client> result = clientService.findByName(missing);

        //Then
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_returnsClientList(){
        //Given
        Long id = 1L;
        Client client = new Client("Ion", "0734654749", "ion@test.ro");
        client.setId(id);
        when(clientRepository.findAll()).thenReturn(List.of(client));

        //When
        List<Client> result = clientService.findAll();

        //Then
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Ion", result.get(0).getName());
        assertEquals("0734654749", result.get(0).getPhone());
        assertEquals("ion@test.ro", result.get(0).getEmail());
    }

    @Test
    void findAllActive_returnsClientList_withActive(){
        //Given
        Long id = 1L;
        Client client = new Client("Ion", "0734654749", "ion@test.ro");
        client.setId(id);
        client.setIsActive(true);
        when(clientRepository.findByIsActiveIsTrue()).thenReturn(List.of(client));

        //When
        List<Client> activeClient = clientService.findAllActive();

        //Then
        assertEquals(1, activeClient.size());
        assertEquals(true, activeClient.get(0).getIsActive());
    }

    @Test
    void findAllInactive_returnsClientList_withInactive(){
        //Given
        Long id = 1L;
        Client client = new Client("Ion", "0734654749", "ion@test.ro");
        client.setId(id);
        client.setIsActive(false);
        when(clientRepository.findByIsActiveIsFalse()).thenReturn(List.of(client));

        //When
        List<Client> activeClient = clientService.findAllInactive();

        //Then
        assertEquals(1, activeClient.size());
        assertEquals(false, activeClient.get(0).getIsActive());
    }

    @Test
    void deactivate_withValidId_setIsInactiveAndSaves(){
        //Given
        Long id = 1L;
        Client client = new Client("Ion", "0734654749", "ion@test.ro");
        client.setId(id);
        client.setIsActive(true);
        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        //When
        clientService.deactivate(client.getId());

        //Then
        assertFalse(client.getIsActive());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void deactivate_withInvalidId_throwsEntityNotFoundException(){
        //Given
        Long notFound = 999L;
        when(clientRepository.findById(notFound)).thenReturn(Optional.empty());

        //When

        //Then
        assertThrows(EntityNotFoundException.class, ()->clientService.deactivate(notFound));
    }
}
