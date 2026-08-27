package com.hairhub.backend.controller;

import com.hairhub.backend.dto.ClientCreateDTO;
import com.hairhub.backend.dto.ClientResponseDTO;
import com.hairhub.backend.entity.Client;
import com.hairhub.backend.exceptions.EntityNotFoundException;
import com.hairhub.backend.mapper.ClientMapper;
import com.hairhub.backend.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService clientService;

    @MockitoBean
    private ClientMapper clientMapper;

    @BeforeEach
    void setUp() {
        Client client = mock(Client.class);
        when(client.getId()).thenReturn(1L);
        when(client.getName()).thenReturn("Ion");
        when(client.getPhone()).thenReturn("0745154783");
        when(client.getEmail()).thenReturn("ion@test.ro");
        when(client.getIsActive()).thenReturn(true);
    }

    @Test
    void postClient_withValidData_returns201() throws Exception {
        //Given
        ClientCreateDTO inputDTO = new ClientCreateDTO("Ion", "0745154783", "ion@test.ro");
        Client saved = new Client("Ion", "0745154783", "ion@test.ro");
        ClientResponseDTO outputDTO = new ClientResponseDTO(1L, "Ion", "0745154783", "ion@test.ro", true);

        when(clientService.create(inputDTO)).thenReturn(saved);
        when(clientMapper.toClientDTO(saved)).thenReturn(outputDTO);

        //When //Then
        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Ion"))
                .andExpect(jsonPath("$.phone").value("0745154783"))
                .andExpect(jsonPath("$.email").value("ion@test.ro"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void postClient_withInvalidName_returns400(String invalidName) throws Exception {
        //Given
        ClientCreateDTO inputDTO = new ClientCreateDTO(invalidName, "0745154783", "ion@test.ro");

        //When //Then
        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchClients_withNullParams_returnsEmptyList() throws Exception {
        //Given
        when(clientService.search(null, null, null)).thenReturn(List.of());

        //When //Then
        mockMvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void searchClients_withNotFoundParams_returnsEmptyList() throws Exception {
        //Given
        when(clientService.search("Vasile", "2547854254", "null@g.c")).thenReturn(List.of());

        //When //Then
        mockMvc.perform(get("/client")
                        .param("name", "Vasile")
                        .param("phone", "2547854254")
                        .param("email", "null@g.c"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void searchClients_withFoundName_returnsClientsWithNameList() throws Exception {
        //Given
        ClientResponseDTO responseDTO = new ClientResponseDTO(
                1L, "Vasile", "2547854254", "null@g.c", true);
        Client result = new Client("Vasile", "2547854254", "null@g.c");
        when(clientService.search("Vasile", null, null)).thenReturn(List.of(result));
        when(clientMapper.toClientDTO(result)).thenReturn(responseDTO);

        //When //Then
        mockMvc.perform(get("/client")
                        .param("name", "Vasile"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Vasile"))
                .andExpect(jsonPath("$[0].phone").value("2547854254"))
                .andExpect(jsonPath("$[0].email").value("null@g.c"))
                .andExpect(jsonPath("$[0].isActive").value(true));
    }

    @Test
    void searchClients_withFoundPhone_returnsClientWithPhone() throws Exception {
        //Given
        ClientResponseDTO responseDTO = new ClientResponseDTO(
                1L, "Vasile", "2547854254", "null@g.c", true);
        Client result = new Client("Vasile", "2547854254", "null@g.c");
        when(clientService.search(null, "2547854254", null)).thenReturn(Collections.singletonList(result));
        when(clientMapper.toClientDTO(result)).thenReturn(responseDTO);

        //When //Then
        mockMvc.perform(get("/client")
                        .param("phone", "2547854254"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Vasile"))
                .andExpect(jsonPath("$[0].phone").value("2547854254"))
                .andExpect(jsonPath("$[0].email").value("null@g.c"))
                .andExpect(jsonPath("$[0].isActive").value(true));

    }

    @Test
    void searchClients_withFoundEmail_returnsClientWithEmail() throws Exception {
        //Given
        ClientResponseDTO responseDTO = new ClientResponseDTO(
                1L, "Vasile", "2547854254", "null@g.c", true);
        Client result = new Client("Vasile", "2547854254", "null@g.c");
        when(clientService.search(null, null, "null@g.c")).thenReturn(Collections.singletonList(result));
        when(clientMapper.toClientDTO(result)).thenReturn(responseDTO);

        //When //Then
        mockMvc.perform(get("/client")
                        .param("email", "null@g.c"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Vasile"))
                .andExpect(jsonPath("$[0].phone").value("2547854254"))
                .andExpect(jsonPath("$[0].email").value("null@g.c"))
                .andExpect(jsonPath("$[0].isActive").value(true));

    }

    @Test
    void getClientById_withValidId_returns200() throws Exception {
        //Given
        ClientResponseDTO responseDTO = new ClientResponseDTO(
                1L, "Vasile", "2547854254", "null@g.c", true);
        Client result = new Client("Vasile", "2547854254", "null@g.c");
        when(clientService.findById(1L)).thenReturn(result);
        when(clientMapper.toClientDTO(result)).thenReturn(responseDTO);

        //When //Then
        mockMvc.perform(get("/client/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Vasile"))
                .andExpect(jsonPath("$.phone").value("2547854254"))
                .andExpect(jsonPath("$.email").value("null@g.c"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void getClientById_withNonExistentId_returns404() throws Exception {
        //Given
        when(clientService.findById(999L))
                .thenThrow(new EntityNotFoundException("Client with id 999 not found"));

        //When //Then
        mockMvc.perform(get("/client/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllActive_withTwoActiveClients_returnsListOfTwo() throws Exception {
        //Given
        Client result1 = new Client("Ion", "2547854254", "notnull@g.c");
        Client result2 = new Client("Vasile", "2547854254", "null@g.c");

        ClientResponseDTO responseDTO1 = new ClientResponseDTO(
                1L, "Ion", "2547854254", "notnull@g.c", true);
        ClientResponseDTO responseDTO2 = new ClientResponseDTO(
                2L, "Vasile", "2547854254", "null@g.c", true);

        when(clientService.findAllActive()).thenReturn(List.of(result1, result2));
        when(clientMapper.toClientDTO(result1)).thenReturn(responseDTO1);
        when(clientMapper.toClientDTO(result2)).thenReturn(responseDTO2);

        mockMvc.perform(get("/client/active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Ion"))
                .andExpect(jsonPath("$[0].isActive").value(true))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Vasile"))
                .andExpect(jsonPath("$[1].isActive").value(true));
    }

    @Test
    void findAllActive_withTwoInactiveClients_returnsListOfTwo() throws Exception {
        //Given
        Client result1 = new Client("Ion", "2547854254", "notnull@g.c");
        Client result2 = new Client("Vasile", "2547854254", "null@g.c");

        ClientResponseDTO responseDTO1 = new ClientResponseDTO(
                1L, "Ion", "2547854254", "notnull@g.c", false);
        ClientResponseDTO responseDTO2 = new ClientResponseDTO(
                2L, "Vasile", "2547854254", "null@g.c", false);

        when(clientService.findAllInactive()).thenReturn(List.of(result1, result2));
        when(clientMapper.toClientDTO(result1)).thenReturn(responseDTO1);
        when(clientMapper.toClientDTO(result2)).thenReturn(responseDTO2);

        mockMvc.perform(get("/client/inactive"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Ion"))
                .andExpect(jsonPath("$[0].isActive").value(false))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Vasile"))
                .andExpect(jsonPath("$[1].isActive").value(false));
    }

    @Test
    void updateClient_withValidData_returns200() throws Exception {
        //Given
        ClientCreateDTO inputDTO = new ClientCreateDTO("Vasile", "0745111111", "vasile@test.ro");
        Client existing = new Client("Ion", "0745154783", "ion@test.ro");
        Client updated = new Client("Vasile", "0745111111", "vasile@test.ro");
        ClientResponseDTO responseDTO = new ClientResponseDTO(1L, "Vasile", "0745111111", "vasile@test.ro", true);

        when(clientService.findById(1L)).thenReturn(existing);
        when(clientService.update(existing)).thenReturn(updated);
        when(clientMapper.toClientDTO(updated)).thenReturn(responseDTO);

        //When //Then
        mockMvc.perform(put("/client/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Vasile"))
                .andExpect(jsonPath("$.phone").value("0745111111"))
                .andExpect(jsonPath("$.email").value("vasile@test.ro"));
    }

    @Test
    void updateClient_withNonExistentId_returns404() throws Exception {
        //Given
        ClientCreateDTO inputDTO = new ClientCreateDTO("Vasile", "0745111111", "vasile@test.ro");
        when(clientService.findById(999L))
                .thenThrow(new EntityNotFoundException("Client with id 999 not found"));

        //When //Then
        mockMvc.perform(put("/client/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isNotFound());
    }
}