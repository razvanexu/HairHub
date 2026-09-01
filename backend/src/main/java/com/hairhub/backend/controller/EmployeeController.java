package com.hairhub.backend.controller;

import com.hairhub.backend.dto.EmployeeCreateDTO;
import com.hairhub.backend.dto.EmployeeResponseDTO;
import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.mapper.EmployeeMapper;
import com.hairhub.backend.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> postEmployee(@Valid @RequestBody EmployeeCreateDTO dto) {
        Employee saved = employeeService.create(dto);
        return ResponseEntity.status(201).body(employeeMapper.toEmployeeDTO(saved));
    }

    @GetMapping()
    public ResponseEntity<List<EmployeeResponseDTO>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email) {
        List<EmployeeResponseDTO> foundEmployees = employeeService
                .search(name, phone, email)
                .stream()
                .map(employeeMapper::toEmployeeDTO)
                .toList();
        return ResponseEntity.ok(foundEmployees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);
        EmployeeResponseDTO dto = employeeMapper.toEmployeeDTO(employee);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/active")
    public ResponseEntity<List<EmployeeResponseDTO>> findAllActive() {
        List<EmployeeResponseDTO> activeEmployees = employeeService
                .findAllActive()
                .stream().map(employeeMapper::toEmployeeDTO)
                .toList();
        return ResponseEntity.ok(activeEmployees);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<EmployeeResponseDTO>> findAllInactive() {
        List<EmployeeResponseDTO> inactiveEmployees = employeeService
                .findAllInactive()
                .stream().map(employeeMapper::toEmployeeDTO)
                .toList();
        return ResponseEntity.ok(inactiveEmployees);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@Valid @RequestBody EmployeeCreateDTO dto,
                                                              @PathVariable Long id) {
        Employee existing = employeeService.findById(id);
        employeeMapper.updateEmployee(existing, dto);
        Employee updated = employeeService.update(existing);
        return ResponseEntity.ok(employeeMapper.toEmployeeDTO(updated));
    }
}
