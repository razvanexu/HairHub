package com.hairhub.backend.controller;

import com.hairhub.backend.dto.EmployeeCreateDTO;
import com.hairhub.backend.dto.EmployeeResponseDTO;
import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.enums.EmployeeRole;
import com.hairhub.backend.exceptions.EntityNotFoundException;
import com.hairhub.backend.mapper.EmployeeMapper;
import com.hairhub.backend.service.EmployeeService;
import jakarta.validation.ValidationException;
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

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private EmployeeMapper employeeMapper;

    @BeforeEach
    void setUp() {
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(1L);
        when(employee.getName()).thenReturn("Ion");
        when(employee.getPhone()).thenReturn("0745154783");
        when(employee.getEmail()).thenReturn("ion@test.ro");
        when(employee.getIsActive()).thenReturn(true);
    }

    @Test
    void postEmployee_withValidData_returns201() throws Exception {
        //Given
        EmployeeCreateDTO inputDTO = new EmployeeCreateDTO(
                "Ion", "0745154783", "ion@test.ro", EmployeeRole.FRIZER);
        Employee saved = new Employee("Ion", "0745154783", "ion@test.ro", EmployeeRole.FRIZER);
        EmployeeResponseDTO outputDTO = new EmployeeResponseDTO(
                1L, "Ion", "0745154783", "ion@test.ro", EmployeeRole.FRIZER, true);

        when(employeeService.create(inputDTO)).thenReturn(saved);
        when(employeeMapper.toEmployeeDTO(saved)).thenReturn(outputDTO);

        //When //Then
        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Ion"))
                .andExpect(jsonPath("$.phone").value("0745154783"))
                .andExpect(jsonPath("$.email").value("ion@test.ro"))
                .andExpect(jsonPath("$.role").value(EmployeeRole.FRIZER.toString()))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void postEmployee_withInvalidName_returns400(String invalidName) throws Exception {
        //Given
        EmployeeCreateDTO inputDTO = new EmployeeCreateDTO(
                invalidName, "0745154783", "ion@test.ro", EmployeeRole.FRIZER);

        //When //Then
        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void postEmployee_withInvalidPhone_returns400(String invalidPhone) throws Exception {
        //Given
        EmployeeCreateDTO inputDTO = new EmployeeCreateDTO(
                "Ion", invalidPhone, "ion@test.ro", EmployeeRole.FRIZER);

        //When //Then
        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "not-an-email"})
    void postEmployee_withInvalidEmail_returns400(String invalidEmail) throws Exception {
        //Given
        EmployeeCreateDTO inputDTO = new EmployeeCreateDTO(
                "Ion", "0745154783", invalidEmail, EmployeeRole.FRIZER);

        //When //Then
        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postEmployee_withNullRole_returns400() throws Exception {
        //Given
        EmployeeCreateDTO inputDTO = new EmployeeCreateDTO(
                "Ion", "0745154783", "ion@test.ro", null);

        //When //Then
        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchEmployees_withNullParams_returnsEmptyList() throws Exception {
        //Given
        when(employeeService.search(null, null, null, null)).thenReturn(List.of());

        //When //Then
        mockMvc.perform(get("/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void searchEmployees_withNotFoundParams_returnsEmptyList() throws Exception {
        //Given
        when(employeeService.search(
                "Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER))
                .thenReturn(List.of());

        //When //Then
        mockMvc.perform(get("/employee")
                        .param("name", "Vasile")
                        .param("phone", "2547854254")
                        .param("email", "null@g.c")
                        .param("role", String.valueOf(EmployeeRole.FRIZER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void searchEmployees_withFoundName_returnsEmployeesWithNameList() throws Exception {
        //Given
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(
                1L, "Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER, true);
        Employee result = new Employee("Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER);
        when(employeeService.search("Vasile", null, null, null)).thenReturn(List.of(result));
        when(employeeMapper.toEmployeeDTO(result)).thenReturn(responseDTO);

        //When //Then
        mockMvc.perform(get("/employee")
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
                .andExpect(jsonPath("$[0].role").value(EmployeeRole.FRIZER.toString()))
                .andExpect(jsonPath("$[0].isActive").value(true));
    }

    @Test
    void searchEmployees_withFoundPhone_returnsEmployeeWithPhone() throws Exception {
        //Given
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(
                1L, "Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER, true);
        Employee result = new Employee("Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER);
        when(employeeService.search(null, "2547854254", null, null)).thenReturn(Collections.singletonList(result));
        when(employeeMapper.toEmployeeDTO(result)).thenReturn(responseDTO);

        //When //Then
        mockMvc.perform(get("/employee")
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
    void searchEmployees_withFoundEmail_returnsEmployeeWithEmail() throws Exception {
        //Given
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(
                1L, "Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER, true);
        Employee result = new Employee("Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER);
        when(employeeService.search(null, null, "null@g.c", null)).thenReturn(Collections.singletonList(result));
        when(employeeMapper.toEmployeeDTO(result)).thenReturn(responseDTO);

        //When //Then
        mockMvc.perform(get("/employee")
                        .param("email", "null@g.c"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Vasile"))
                .andExpect(jsonPath("$[0].phone").value("2547854254"))
                .andExpect(jsonPath("$[0].email").value("null@g.c"))
                .andExpect(jsonPath("$[0].role").value(EmployeeRole.FRIZER.toString()))
                .andExpect(jsonPath("$[0].isActive").value(true));

    }

    @Test
    void searchEmployees_withFoundRole_returnsEmployeeWithRole() throws Exception {
        //Given
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(
                1L, "Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER, true);
        Employee result = new Employee("Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER);
        when(employeeService.search(
                null, null, null, EmployeeRole.FRIZER))
                .thenReturn(List.of(result));
        when(employeeMapper.toEmployeeDTO(result)).thenReturn(responseDTO);

        //When //Then
        mockMvc.perform(get("/employee")
                        .param("role", "FRIZER"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Vasile"))
                .andExpect(jsonPath("$[0].phone").value("2547854254"))
                .andExpect(jsonPath("$[0].email").value("null@g.c"))
                .andExpect(jsonPath("$[0].role").value(EmployeeRole.FRIZER.toString()))
                .andExpect(jsonPath("$[0].isActive").value(true));

    }

    @Test
    void searchEmployees_withInvalidRole_returns400() throws Exception {
        //Given //When //Then
        mockMvc.perform(get("/employee")
                        .param("role", "MANAGER_INEXISTENT"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postEmployee_withInvalidPhoneFormat_returns400() throws Exception {
        //Given
        EmployeeCreateDTO inputDTO = new EmployeeCreateDTO(
                "Ion", "+40725475548", "ion@test.ro", EmployeeRole.FRIZER);
        when(employeeService.create(inputDTO))
                .thenThrow(new ValidationException(
                        "Phone must be a valid Romanian mobile number (07xxxxxxxx)"));

        //When //Then
        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getEmployeeById_withValidId_returns200() throws Exception {
        //Given
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(
                1L, "Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER, true);
        Employee result = new Employee("Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER);
        when(employeeService.findById(1L)).thenReturn(result);
        when(employeeMapper.toEmployeeDTO(result)).thenReturn(responseDTO);

        //When //Then
        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Vasile"))
                .andExpect(jsonPath("$.phone").value("2547854254"))
                .andExpect(jsonPath("$.email").value("null@g.c"))
                .andExpect(jsonPath("$.role").value(EmployeeRole.FRIZER.toString()))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void getEmployeeById_withNonExistentId_returns404() throws Exception {
        //Given
        when(employeeService.findById(999L))
                .thenThrow(new EntityNotFoundException("Employee with id 999 not found"));

        //When //Then
        mockMvc.perform(get("/employee/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllActive_withTwoActiveEmployees_returnsListOfTwo() throws Exception {
        //Given
        Employee result1 = new Employee("Ion", "2547854254", "notnull@g.c", EmployeeRole.FRIZER);
        Employee result2 = new Employee("Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER);

        EmployeeResponseDTO responseDTO1 = new EmployeeResponseDTO(
                1L, "Ion", "2547854254", "notnull@g.c", EmployeeRole.FRIZER, true);
        EmployeeResponseDTO responseDTO2 = new EmployeeResponseDTO(
                2L, "Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER, true);

        when(employeeService.findAllActive()).thenReturn(List.of(result1, result2));
        when(employeeMapper.toEmployeeDTO(result1)).thenReturn(responseDTO1);
        when(employeeMapper.toEmployeeDTO(result2)).thenReturn(responseDTO2);

        mockMvc.perform(get("/employee/active"))
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
    void findAllInactive_withTwoInactiveEmployees_returnsListOfTwo() throws Exception {
        //Given
        Employee result1 = new Employee("Ion", "2547854254", "notnull@g.c", EmployeeRole.FRIZER);
        Employee result2 = new Employee("Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER);

        EmployeeResponseDTO responseDTO1 = new EmployeeResponseDTO(
                1L, "Ion", "2547854254", "notnull@g.c", EmployeeRole.FRIZER, false);
        EmployeeResponseDTO responseDTO2 = new EmployeeResponseDTO(
                2L, "Vasile", "2547854254", "null@g.c", EmployeeRole.FRIZER, false);

        when(employeeService.findAllInactive()).thenReturn(List.of(result1, result2));
        when(employeeMapper.toEmployeeDTO(result1)).thenReturn(responseDTO1);
        when(employeeMapper.toEmployeeDTO(result2)).thenReturn(responseDTO2);

        mockMvc.perform(get("/employee/inactive"))
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
    void updateEmployee_withValidData_returns200() throws Exception {
        //Given
        EmployeeCreateDTO inputDTO = new EmployeeCreateDTO(
                "Vasile", "0745111111", "vasile@test.ro", EmployeeRole.FRIZER);
        Employee existing = new Employee("Ion", "0745154783", "ion@test.ro", EmployeeRole.FRIZER);
        Employee updated = new Employee("Vasile", "0745111111", "vasile@test.ro", EmployeeRole.FRIZER);
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(
                1L, "Vasile", "0745111111", "vasile@test.ro", EmployeeRole.FRIZER, true);

        when(employeeService.findById(1L)).thenReturn(existing);
        when(employeeService.update(existing)).thenReturn(updated);
        when(employeeMapper.toEmployeeDTO(updated)).thenReturn(responseDTO);

        //When //Then
        mockMvc.perform(put("/employee/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Vasile"))
                .andExpect(jsonPath("$.phone").value("0745111111"))
                .andExpect(jsonPath("$.email").value("vasile@test.ro"));
    }

    @Test
    void updateEmployee_withNonExistentId_returns404() throws Exception {
        //Given
        EmployeeCreateDTO inputDTO = new EmployeeCreateDTO(
                "Vasile", "0745111111", "vasile@test.ro", EmployeeRole.FRIZER);
        when(employeeService.findById(999L))
                .thenThrow(new EntityNotFoundException("Employee with id 999 not found"));

        //When //Then
        mockMvc.perform(put("/employee/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isNotFound());
    }
}