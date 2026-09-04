package com.hairhub.backend.mapper;

import com.hairhub.backend.dto.EmployeeCreateDTO;
import com.hairhub.backend.dto.EmployeeResponseDTO;
import com.hairhub.backend.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public Employee toEmployee(EmployeeCreateDTO dto) {
        return new Employee(dto.name(), dto.phone(), dto.email());
    }

    public EmployeeResponseDTO toEmployeeDTO(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getIsActive()
        );
    }

    public void updateEmployee(Employee existing, EmployeeCreateDTO dto) {
        existing.setName(dto.name());
        existing.setPhone(dto.phone());
        existing.setEmail(dto.email());
    }
}
