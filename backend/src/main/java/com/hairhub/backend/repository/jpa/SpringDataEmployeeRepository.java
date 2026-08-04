package com.hairhub.backend.repository.jpa;

import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.enums.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataEmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByPhone(String phone);
    List<Employee> findByName(String name);
    List<Employee> findByRole(EmployeeRole role);
    List<Employee> findByIsActiveIsTrue();
    List<Employee> findByIsActiveIsFalse();
}
