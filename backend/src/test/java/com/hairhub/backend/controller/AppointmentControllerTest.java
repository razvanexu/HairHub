package com.hairhub.backend.controller;

import com.hairhub.backend.dto.AppointmentCreateDTO;
import com.hairhub.backend.dto.AppointmentResponseDTO;
import com.hairhub.backend.entity.Appointment;
import com.hairhub.backend.entity.Client;
import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.ServiceType;
import com.hairhub.backend.entity.enums.AppointmentStatus;
import com.hairhub.backend.exceptions.EntityNotFoundException;
import com.hairhub.backend.mapper.AppointmentMapper;
import com.hairhub.backend.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {
    private Client client;
    private Employee employee;
    private ServiceType serviceType;
    private LocalDateTime startTime;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AppointmentService appointmentService;

    @MockitoBean
    private AppointmentMapper appointmentMapper;

    @BeforeEach
    void setUp() {
        client = mock(Client.class);
        when(client.getId()).thenReturn(1L);
        employee = mock(Employee.class);
        when(employee.getId()).thenReturn(1L);
        serviceType = mock(ServiceType.class);
        when(serviceType.getId()).thenReturn(1L);
        startTime = LocalDateTime.of(2026, 9, 1, 8, 0, 0);
    }

    @Test
    void getAppointmens_returnsEmptyList() throws Exception {
        //Given
        when(appointmentService.search(null, null, null)).thenReturn(List.of());

        //When //Then
        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAppointments_returnsListWithItems() throws Exception {
        //Given
        Appointment appointment = new Appointment(client, employee, serviceType, startTime,
                30, AppointmentStatus.PENDING);
        AppointmentResponseDTO dto = new AppointmentResponseDTO(1L, 1L, 1L, 1L,
                startTime, 30, AppointmentStatus.PENDING);

        when(appointmentService.search(null, null, null)).thenReturn(List.of(appointment));
        when(appointmentMapper.toAppointmentDTO(appointment)).thenReturn(dto);

        //When //Then
        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].clientId").value(1))
                .andExpect(jsonPath("$[0].employeeId").value(1))
                .andExpect(jsonPath("$[0].serviceTypeId").value(1))
                .andExpect(jsonPath("$[0].duration").value(30))
                .andExpect(jsonPath("$[0].appointmentStatus").value("PENDING"));
    }

    @Test
    void getAppointmentById__withExistingId_returns200() throws Exception {
        //Given
        Appointment appointment = new Appointment(client, employee, serviceType, startTime,
                30, AppointmentStatus.PENDING);

        AppointmentResponseDTO dto = new AppointmentResponseDTO(1L, 1L, 1L, 1L,
                startTime, 30, AppointmentStatus.PENDING);

        when(appointmentService.findById(1L)).thenReturn(appointment);
        when(appointmentMapper.toAppointmentDTO(appointment)).thenReturn(dto);

        //When //Then
        mockMvc.perform(get("/appointments/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.employeeId").value(1))
                .andExpect(jsonPath("$.serviceTypeId").value(1))
                .andExpect(jsonPath("$.duration").value(30))
                .andExpect(jsonPath("$.appointmentStatus").value("PENDING"));
    }

    @Test
    void getAppoinmentById_withNonExistentId_returns404() throws Exception {
        //Given
        when(appointmentService.findById(999L))
                .thenThrow(new EntityNotFoundException("Appointment with id 999 not found"));

        //When //Then
        mockMvc.perform(get("/appointments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postAppointment_withValidData_returns201() throws Exception {
        //Given
        AppointmentCreateDTO inputDTO = new AppointmentCreateDTO(
                1L, 1L, 1L, startTime, 30);
        Appointment saved = new Appointment(
                client, employee, serviceType, startTime, 30, AppointmentStatus.PENDING);
        AppointmentResponseDTO outputDTO = new AppointmentResponseDTO(
                1L, 1L, 1L, 1L,
                startTime, 30, AppointmentStatus.PENDING);

        when(appointmentService.create(inputDTO)).thenReturn(saved);
        when(appointmentMapper.toAppointmentDTO(saved)).thenReturn(outputDTO);

        //When //Then
        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.employeeId").value(1))
                .andExpect(jsonPath("$.serviceTypeId").value(1))
                .andExpect(jsonPath("$.duration").value(30))
                .andExpect(jsonPath("$.appointmentStatus").value("PENDING"));
    }

    @Test
    void postAppointment_withNullEmployeeId_returns400() throws Exception {
        //Given
        AppointmentCreateDTO invalidDTO = new AppointmentCreateDTO(1L, null,
                1L, startTime, 30);

        //When //Then
        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postAppointment_withNullServiceTypeId_returns400() throws Exception {
        //Given
        AppointmentCreateDTO invalidDTO = new AppointmentCreateDTO(1L, 1L,
                null, startTime, 30);

        //When //Then
        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postAppointment_withNullStartTime_returns400() throws Exception {
        //Given
        AppointmentCreateDTO invalidDTO = new AppointmentCreateDTO(1L, 1L,
                1L, null, 30);

        //When //Then
        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postAppointment_withNullDuration_returns400() throws Exception {
        //Given
        AppointmentCreateDTO invalidDTO = new AppointmentCreateDTO(1L, 1L,
                1L, startTime, null);

        //When //Then
        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelAppointment_withValidId_returns204() throws Exception {
        //Given
        Long id = 1L;

        //When //Then
        mockMvc.perform(patch("/appointments/1/cancel", id))
                .andExpect(status().isNoContent());
        verify(appointmentService).cancel(id);
    }

    @Test
    void cancelAppointment_withInvalidId_returns404() throws Exception {
        //Given
        Long id = 999L;
        doThrow(new EntityNotFoundException("Appointment with id 999 not found"))
                .when(appointmentService).cancel(id);

        //When //Then
        mockMvc.perform(patch("/appointments/999/cancel", id))
                .andExpect(status().isNotFound());
    }
}

