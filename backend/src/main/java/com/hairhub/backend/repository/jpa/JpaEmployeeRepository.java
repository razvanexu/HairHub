package com.hairhub.backend.repository.jpa;

import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.enums.EmployeeRole;
import com.hairhub.backend.repository.EmployeeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaEmployeeRepository implements EmployeeRepository {

    private final SpringDataEmployeeRepository springDataEmployeeRepository;

    public JpaEmployeeRepository(SpringDataEmployeeRepository springDataEmployeeRepository){
        this.springDataEmployeeRepository = springDataEmployeeRepository;
    }

    @Override
    public Employee save(Employee employee) {
        return springDataEmployeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return springDataEmployeeRepository.findById(id);
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return springDataEmployeeRepository.findByEmail(email);
    }

    @Override
    public Optional<Employee> findByPhone(String phone) {
        return springDataEmployeeRepository.findByPhone(phone);
    }

    @Override
    public List<Employee> findByName(String name) {
        return springDataEmployeeRepository.findByName(name);
    }

    @Override
    public List<Employee> findByRole(EmployeeRole role){
        return springDataEmployeeRepository.findByRole(role);
    }

    @Override
    public List<Employee> findByIsActiveIsTrue() {
        return springDataEmployeeRepository.findByIsActiveIsTrue();
    }

    @Override
    public List<Employee> findByIsActiveIsFalse() {
        return springDataEmployeeRepository.findByIsActiveIsFalse();
    }

    @Override
    public List<Employee> findAll() {
        return springDataEmployeeRepository.findAll();
    }
}
