package com.hairhub.backend.controller;

import com.hairhub.backend.dto.ServiceTypeDTO;
import com.hairhub.backend.entity.ServiceType;
import com.hairhub.backend.exceptions.EntityNotFoundException;
import com.hairhub.backend.mapper.ServiceTypeMapper;
import com.hairhub.backend.service.ServiceTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServiceTypeController.class)
class ServiceTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceTypeService serviceTypeService;

    @MockitoBean
    private ServiceTypeMapper serviceTypeMapper;

    @Test
    void getServiceTypes_returnsEmptyList() throws Exception {
        //Given
        when(serviceTypeService.findAll()).thenReturn(List.of());

        //When //Then
        mockMvc.perform(get("/service-type"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getServiceTypes_returnsListWithItems() throws Exception {
        //Given
        ServiceType serviceType = new ServiceType("Tuns", 30);
        serviceType.setId(1L);
        ServiceTypeDTO serviceTypeDTO = new ServiceTypeDTO(1L, "Tuns", 30);

        when(serviceTypeService.findAll()).thenReturn(List.of(serviceType));
        when(serviceTypeMapper.toServiceTypeDTO(serviceType)).thenReturn(serviceTypeDTO);

        //When //Then
        mockMvc.perform(get("/service-type"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Tuns"))
                .andExpect(jsonPath("$[0].duration").value(30));
    }

    @Test
    void getServiceTypeById_withExistingId_returns200() throws Exception {
        //Given
        ServiceType serviceType = new ServiceType("Tuns", 30);
        serviceType.setId(1L);
        ServiceTypeDTO serviceTypeDTO = new ServiceTypeDTO(1L, "Tuns", 30);

        when(serviceTypeService.findById(1L)).thenReturn(serviceType);
        when(serviceTypeMapper.toServiceTypeDTO(serviceType)).thenReturn(serviceTypeDTO);

        //When //Then
        mockMvc.perform(get("/service-type/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Tuns"))
                .andExpect(jsonPath("$.duration").value(30));
    }

    @Test
    void getServiceTypeByName_withExistingName_returns200() throws Exception {
        //Given
        ServiceType serviceType = new ServiceType("Tuns", 30);
        serviceType.setId(1L);
        ServiceTypeDTO serviceTypeDTO = new ServiceTypeDTO(1L, "Tuns", 30);

        when(serviceTypeService.findByName("Tuns")).thenReturn(serviceType);
        when(serviceTypeMapper.toServiceTypeDTO(serviceType)).thenReturn(serviceTypeDTO);

        //When //Then
        mockMvc.perform(get("/service-type/name/Tuns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Tuns"))
                .andExpect(jsonPath("$.duration").value(30));
    }

    @Test
    void getServiceTypeById_withNonExistentId_returns404() throws Exception {
        //Given

        when(serviceTypeService.findById(999L))
                .thenThrow(new EntityNotFoundException("Service type with id 999 not found"));

        //When //Then
        mockMvc.perform(get("/service-type/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getServiceTypeById_withNonExistentName_returns404() throws Exception {
        //Given

        when(serviceTypeService.findByName("Coafor"))
                .thenThrow(new EntityNotFoundException("Service type with name Coafor not found"));

        //When //Then
        mockMvc.perform(get("/service-type/name/Coafor"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postServiceType_withValidData_returns201()  throws Exception {
        //Given
        ServiceTypeDTO inputDTO = new ServiceTypeDTO(null, "Tuns", 30);
        ServiceType entity = new ServiceType("Tuns", 30);
        ServiceType saved = new ServiceType("Tuns", 30);
        saved.setId(1L);
        ServiceTypeDTO outputDto = new ServiceTypeDTO(1L, "Tuns", 30);

        when(serviceTypeMapper.toServiceType(inputDTO)).thenReturn(entity);
        when(serviceTypeService.create(entity)).thenReturn(saved);
        when(serviceTypeMapper.toServiceTypeDTO(saved)).thenReturn(outputDto);

        //When //Then
        mockMvc.perform(post("/service-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Tuns"))
                .andExpect(jsonPath("$.duration").value(30));
    }

    @Test
    void postServiceType_withEmptyName_returns400()  throws Exception{
        //Given
        ServiceTypeDTO inputDTO = new ServiceTypeDTO(null, "", 30);

        //When //Then
        mockMvc.perform(post("/service-type")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postServiceType_withNullName_returns400()  throws Exception{
        //Given
        ServiceTypeDTO inputDTO = new ServiceTypeDTO(null, null, 30);

        //When //Then
        mockMvc.perform(post("/service-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postServiceType_withNullDuration_returns400()  throws Exception{
        //Given
        ServiceTypeDTO inputDTO = new ServiceTypeDTO(null, "Tuns", null);

        //When //Then
        mockMvc.perform(post("/service-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateServiceType_withValidData_returns200() throws Exception{
        //Given
        Long id = 1L;
        ServiceType existing = new ServiceType("Tuns", 30);
        existing.setId(id);

        ServiceTypeDTO inputDTO = new ServiceTypeDTO(null,"Coafor", 60);

        ServiceType updated = new ServiceType("Coafor", 60);
        updated.setId(id);

        ServiceTypeDTO updatedDTO = new ServiceTypeDTO(id, "Coafor", 60);

        when(serviceTypeService.findById(id)).thenReturn(existing);
        when(serviceTypeService.update(existing)).thenReturn(updated);
        when(serviceTypeMapper.toServiceTypeDTO(updated)).thenReturn(updatedDTO);

        //When //Then
        mockMvc.perform(put("/service-type/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Coafor"))
                .andExpect(jsonPath("$.duration").value(60));
    }

    @Test
    void updateServiceType_withNullName_returns400() throws Exception{
        //Given
        ServiceTypeDTO invalidDTO = new ServiceTypeDTO(1L, null, 60);

        //When //Then
        mockMvc.perform(put("/service-type/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateServiceType_withBlankName_returns400() throws Exception{
        //Given
        ServiceTypeDTO invalidDTO = new ServiceTypeDTO(1L, " ", 60);

        //When //Then
        mockMvc.perform(put("/service-type/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateServiceType_withNonExistentId_returns400() throws Exception{
        //Given
        Long id = 999L;
        ServiceTypeDTO inputDto = new ServiceTypeDTO(null, "Tuns", 30);
        when(serviceTypeService.findById(id))
                .thenThrow(new EntityNotFoundException("Service type with id 999 not found"));

        //When //Then
        mockMvc.perform(put("/service-type/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteServiceType_withValidId_returns204()  throws Exception {
        //Given
        Long id = 1L;

        //When //Then
        mockMvc.perform(delete("/service-type/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteServiceType_withNonExistentId_returns404() throws Exception {
        // Given
        Long id = 999L;
        doThrow(new EntityNotFoundException("Service type with id 999 not found"))
                .when(serviceTypeService).deleteById(id);

        // When + Then
        mockMvc.perform(delete("/service-type/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteServiceType_withValidName_returns204() throws Exception {
        // Given
        String name = "Coafor";

        // When + Then
        mockMvc.perform(delete("/service-type/name/" + name))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteServiceType_withNonExistentName_returns404() throws Exception {
        // Given
        String name = "nonExistent";
        doThrow(new EntityNotFoundException("Service type with name nonExistent not found"))
                .when(serviceTypeService).deleteByName(name);

        // When + Then
        mockMvc.perform(delete("/service-type/name/" + name))
                .andExpect(status().isNotFound());
    }
}
