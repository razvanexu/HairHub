package com.hairhub.backend.repository;

import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.enums.EmployeeRole;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    Employee save (Employee employee);
    Optional<Employee> findById(Long id);
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByPhone(String phone);
    List<Employee> findByName(String name);
    List<Employee> findByRole(EmployeeRole role);
    List<Employee> findByIsActiveIsTrue();
    List<Employee> findByIsActiveIsFalse();
    List<Employee> findAll();
}
