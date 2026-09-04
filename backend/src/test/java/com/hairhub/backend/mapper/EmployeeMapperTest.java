package com.hairhub.backend.mapper;

import com.hairhub.backend.dto.EmployeeCreateDTO;
import com.hairhub.backend.dto.EmployeeResponseDTO;
import com.hairhub.backend.entity.Employee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EmployeeMapperTest {
    private final EmployeeMapper employeeMapper = new EmployeeMapper();

    @Test
    void toEmployee_mapsFieldsCorrectly() {
        //Given
        EmployeeCreateDTO createDTO = new EmployeeCreateDTO("Ion", "0452145874", "Ion@t.ro");

        //When
        Employee result = employeeMapper.toEmployee(createDTO);

        //Then
        assertNull(result.getId());
        assertEquals("Ion", result.getName());
        assertEquals("0452145874", result.getPhone());
        assertEquals("Ion@t.ro", result.getEmail());
        assertTrue(result.getIsActive());
    }

    @Test
    void toEmployeeDTO_mapsFieldsCorrectly() {
        //Given
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(1L);
        when(employee.getName()).thenReturn("Ion");
        when(employee.getPhone()).thenReturn("0745154254");
        when(employee.getEmail()).thenReturn("ion@test.ro");
        when(employee.getIsActive()).thenReturn(true);

        //When
        EmployeeResponseDTO result = employeeMapper.toEmployeeDTO(employee);

        //Then
        assertEquals(1L, result.id());
        assertEquals("Ion", result.name());
        assertEquals("0745154254", result.phone());
        assertEquals("ion@test.ro", result.email());
        assertTrue(result.isActive());
    }

    @Test
    void updateEmployee_updatesWithNewDto() {
        //Given
        Employee existing = new Employee("Ion", "0547254123", "ion@test.ro");
        existing.setIsActive(true);
        EmployeeCreateDTO dto = new EmployeeCreateDTO("Vasile", "0745154368", "vasile@test.ro");

        //When
        employeeMapper.updateEmployee(existing, dto);

        //Then
        assertEquals("Vasile", existing.getName());
        assertEquals("0745154368", existing.getPhone());
        assertEquals("vasile@test.ro", existing.getEmail());
        assertTrue(existing.getIsActive());
    }

    @Test
    void updateEmployee_updatesWithNew_andKeepsIsInactive() {
        //Given
        Employee existing = new Employee("Ion", "0547254123", "ion@test.ro");
        existing.setIsActive(false);
        EmployeeCreateDTO dto = new EmployeeCreateDTO("Vasile", "0745154368", "vasile@test.ro");

        //When
        employeeMapper.updateEmployee(existing, dto);

        //Then
        assertEquals("Vasile", existing.getName());
        assertEquals("0745154368", existing.getPhone());
        assertEquals("vasile@test.ro", existing.getEmail());
        assertFalse(existing.getIsActive());
    }
}
