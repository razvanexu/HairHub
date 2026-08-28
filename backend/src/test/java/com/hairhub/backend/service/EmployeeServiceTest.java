package com.hairhub.backend.service;

import com.hairhub.backend.dto.EmployeeCreateDTO;
import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.enums.EmployeeRole;
import com.hairhub.backend.exceptions.EntityNotFoundException;
import com.hairhub.backend.mapper.EmployeeMapper;
import com.hairhub.backend.repository.EmployeeRepository;
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
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PhoneValidation phoneValidation;

    private EmployeeService employeeService;

    @Mock
    private EmployeeMapper employeeMapper;

    @BeforeEach
    void setup() {
        employeeService = new EmployeeService(employeeRepository, phoneValidation, employeeMapper);
    }

    @Test
    void create_withValidEmployee_savesAndReturnsEmployee() {
        //Given
        EmployeeCreateDTO input = new EmployeeCreateDTO("Ion", "0725475548", "ion@mail.de", EmployeeRole.FRIZER);
        Employee mappedEmployee = new Employee("Ion", "0725475548", "ion@mail.de", EmployeeRole.FRIZER);
        when(employeeMapper.toEmployee(input)).thenReturn(mappedEmployee);
        when(employeeRepository.save(mappedEmployee)).thenReturn(mappedEmployee);

        //When
        Employee result = employeeService.create(input);

        //Then
        assertEquals("Ion", result.getName());
        assertEquals("0725475548", result.getPhone());
        assertEquals("ion@mail.de", result.getEmail());
        assertEquals(EmployeeRole.FRIZER, result.getRole());
        verify(employeeRepository).save(mappedEmployee);
    }

    @Test
    void create_withInvalidPhone_throwsException() {
        //Given
        EmployeeCreateDTO input = new EmployeeCreateDTO(
                "Ion", "+40725475548", "ion@mail.de", EmployeeRole.FRIZER);
        Employee mappedEmployee = new Employee(
                "Ion", "+40725475548", "ion@mail.de", EmployeeRole.FRIZER);
        doThrow(new ValidationException("Invalid Phone"))
                .when(phoneValidation).validate(input.phone());

        //When //Then
        assertThrows(ValidationException.class, () -> employeeService.create(input));
        verify(employeeRepository, never()).save(mappedEmployee);
    }

    @Test
    void update_withValidEmployee_savesAndReturns() {
        //Given
        Employee input = new Employee("Ion", "0725475548", "ion@mail.de", EmployeeRole.FRIZER);
        input.setName("Vasile");
        input.setPhone("0725475548");
        input.setEmail("Vasile@mail.de");
        input.setRole(EmployeeRole.OWNER);
        when(employeeRepository.save(input)).thenReturn(input);

        //When
        Employee result = employeeService.update(input);

        // Then
        assertEquals("Vasile", result.getName());
        assertEquals("0725475548", result.getPhone());
        assertEquals("Vasile@mail.de", result.getEmail());
        assertEquals(EmployeeRole.OWNER, result.getRole());
    }

    @Test
    void update_withInvalidPhone_throwsException() {
        //Given
        Employee input = new Employee("Ion", "+40725475548", "ion@mail.de", EmployeeRole.FRIZER);
        doThrow(new ValidationException("Invalid Phone"))
                .when(phoneValidation).validate(input.getPhone());

        //When //Then
        assertThrows(ValidationException.class, () -> employeeService.update(input));
        verify(employeeRepository, never()).save(input);
    }

    @Test
    void findById_withExistentId_returnsEmployee() {
        //Given
        Long id = 1L;
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(1L);
        when(employee.getName()).thenReturn("Ion");
        when(employee.getPhone()).thenReturn("0734654749");
        when(employee.getEmail()).thenReturn("ion@test.ro");
        when(employee.getRole()).thenReturn(EmployeeRole.FRIZER);
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        //When
        Employee result = employeeService.findById(id);

        //Then
        assertEquals(1L, result.getId());
        assertEquals("Ion", result.getName());
        assertEquals("0734654749", result.getPhone());
        assertEquals("ion@test.ro", result.getEmail());
        assertEquals(EmployeeRole.FRIZER, result.getRole());
    }

    @Test
    void findById_withNonExistentId_throwsEntityNotFoundException() {
        //Given
        Long id = 999L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        //When

        //Then
        assertThrows(EntityNotFoundException.class, () -> employeeService.findById(id));
    }

    @Test
    void findById_negativeId_throwsEntityNotFoundException() {
        //Given
        Long id = -1L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        //When

        //Then
        assertThrows(EntityNotFoundException.class, () -> employeeService.findById(id));
    }

    @Test
    void findByPhone_withExistentPhoneNumber_returnsEmployee() {
        //Given
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(1L);
        when(employee.getName()).thenReturn("Ion");
        when(employee.getPhone()).thenReturn("0734654749");
        when(employee.getEmail()).thenReturn("ion@test.ro");
        when(employee.getRole()).thenReturn(EmployeeRole.FRIZER);
        when(employeeRepository.findByPhone(employee.getPhone())).thenReturn(Optional.of(employee));

        //When
        Employee result = employeeService.findByPhone(employee.getPhone());

        //Then
        assertEquals(1L, result.getId());
        assertEquals("Ion", result.getName());
        assertEquals("0734654749", result.getPhone());
        assertEquals("ion@test.ro", result.getEmail());
        assertEquals(EmployeeRole.FRIZER, result.getRole());
    }

    @Test
    void findByPhone_withNonExistentPhoneNumber_throwsEntityNotFoundException() {
        //Given
        String missing = "0745678418";
        when(employeeRepository.findByPhone(missing)).thenReturn(Optional.empty());

        //When

        //Then
        assertThrows(EntityNotFoundException.class, () -> employeeService.findByPhone(missing));
    }

    @Test
    void findByEmail_withExistentEmail_returnsEmployee() {
        //Given
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(1L);
        when(employee.getName()).thenReturn("Ion");
        when(employee.getPhone()).thenReturn("0734654749");
        when(employee.getEmail()).thenReturn("ion@test.ro");
        when(employee.getRole()).thenReturn(EmployeeRole.FRIZER);
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        //When
        Employee result = employeeService.findByEmail(employee.getEmail());

        //Then
        assertEquals(1L, result.getId());
        assertEquals("Ion", result.getName());
        assertEquals("0734654749", result.getPhone());
        assertEquals("ion@test.ro", result.getEmail());
        assertEquals(EmployeeRole.FRIZER, result.getRole());
    }

    @Test
    void findByEmail_withNonExistentEmail_throwsEntityNotFoundException() {
        //Given
        String missing = "missing@de.ro";
        when(employeeRepository.findByEmail(missing)).thenReturn(Optional.empty());

        //When

        //Then
        assertThrows(EntityNotFoundException.class, () -> employeeService.findByEmail(missing));
    }

    @Test
    void findByName_withExistentName_returnsEmployeeList() {
        //Given
        Employee employee = mock(Employee.class);
        when(employee.getName()).thenReturn("Ion");
        when(employeeRepository.findByName(employee.getName())).thenReturn(List.of(employee));

        //When
        List<Employee> result = employeeService.findByName(employee.getName());

        //Then
        assertEquals(1, result.size());
        assertEquals("Ion", result.get(0).getName());
    }

    @Test
    void findByName_withNonExistentName_returnsEmptyList() {
        //Given
        String missing = "missing";
        when(employeeRepository.findByName(missing)).thenReturn(List.of());

        //When
        List<Employee> result = employeeService.findByName(missing);

        //Then
        assertTrue(result.isEmpty());
    }

    @Test
    void findByRole_withExistentRole_returnsEmployeeList() {
        //Given
        Employee employee = mock(Employee.class);
        when(employee.getRole()).thenReturn(EmployeeRole.FRIZER);
        when(employeeRepository.findByRole(employee.getRole())).thenReturn(List.of(employee));

        //When
        List<Employee> result = employeeService.findByRole(employee.getRole());

        //Then
        assertEquals(1, result.size());
        assertEquals(EmployeeRole.FRIZER, result.get(0).getRole());
    }

    @Test
    void findByRole_withNoMatches_returnsEmptyList() {
        //Given
        when(employeeRepository.findByRole(EmployeeRole.OWNER)).thenReturn(List.of());

        //When
        List<Employee> result = employeeService.findByRole(EmployeeRole.OWNER);

        //Then
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_returnsEmployeeList() {
        //Given
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(1L);
        when(employee.getName()).thenReturn("Ion");
        when(employee.getPhone()).thenReturn("0734654749");
        when(employee.getEmail()).thenReturn("ion@test.ro");
        when(employee.getRole()).thenReturn(EmployeeRole.FRIZER);
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        //When
        List<Employee> result = employeeService.findAll();

        //Then
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Ion", result.get(0).getName());
        assertEquals("0734654749", result.get(0).getPhone());
        assertEquals("ion@test.ro", result.get(0).getEmail());
        assertEquals(EmployeeRole.FRIZER, result.get(0).getRole());
    }

    @Test
    void findAllActive_returnsEmployeeList_withActive() {
        //Given
        Employee employee = mock(Employee.class);
        when(employee.getIsActive()).thenReturn(true);
        when(employeeRepository.findByIsActiveIsTrue()).thenReturn(List.of(employee));

        //When
        List<Employee> activeEmployee = employeeService.findAllActive();

        //Then
        assertEquals(1, activeEmployee.size());
        assertEquals(true, activeEmployee.get(0).getIsActive());
    }

    @Test
    void findAllInactive_returnsEmployeeList_withInactive() {
        //Given
        Employee employee = mock(Employee.class);
        when(employee.getIsActive()).thenReturn(false);
        when(employeeRepository.findByIsActiveIsFalse()).thenReturn(List.of(employee));

        //When
        List<Employee> activeEmployee = employeeService.findAllInactive();

        //Then
        assertEquals(1, activeEmployee.size());
        assertEquals(false, activeEmployee.get(0).getIsActive());
    }

    @Test
    void deactivate_withValidId_setIsInactiveAndSaves() {
        //Given
        Long id = 1L;
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(1L);
        when(employee.getIsActive()).thenReturn(false);
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        //When
        employeeService.deactivate(employee.getId());

        //Then
        assertFalse(employee.getIsActive());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void deactivate_withInvalidId_throwsEntityNotFoundException() {
        //Given
        Long notFound = 999L;
        when(employeeRepository.findById(notFound)).thenReturn(Optional.empty());

        //When

        //Then
        assertThrows(EntityNotFoundException.class, () -> employeeService.deactivate(notFound));
    }

}
