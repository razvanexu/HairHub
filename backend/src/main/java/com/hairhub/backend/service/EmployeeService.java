package com.hairhub.backend.service;

import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.enums.EmployeeRole;
import com.hairhub.backend.exceptions.EntityNotFoundException;
import com.hairhub.backend.repository.EmployeeRepository;
import com.hairhub.backend.service.validators.PhoneValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmployeeService {
    private static final String NOT_FOUND_SUFFIX = " not found";
    private final EmployeeRepository employeeRepository;
    private final PhoneValidation phoneValidation;

    public EmployeeService(EmployeeRepository employeeRepository, PhoneValidation phoneValidation) {
        this.employeeRepository = employeeRepository;
        this.phoneValidation = phoneValidation;
    }

    public Employee create(Employee employee){
        phoneValidation.validate(employee.getPhone());
        Employee saved = employeeRepository.save(employee);
        log.info("[create] Employee with id {} has been created", saved.getId());
        return saved;
    }

    public Employee update(Employee employee){
        phoneValidation.validate(employee.getPhone());
        Employee updated = employeeRepository.save(employee);
        log.info("[update] Employee with id {} has been updated", updated.getId());
        return updated;
    }

    public Employee findById(Long id){
        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[findById] Employee with id {} not found", id);
                    return new EntityNotFoundException("Employee with id " + id + NOT_FOUND_SUFFIX);
                });
    }

    public List<Employee> findByName(String name){
        List<Employee> namedEmployees = employeeRepository.findByName(name);
        log.debug("[findByName] Found {} employee(s)", namedEmployees.size());
        return namedEmployees;
    }

    public List<Employee> findAll(){
        List<Employee> allEmployees = employeeRepository.findAll();
        log.debug("[findAll] Found {} employees(s)", allEmployees.size());
        return allEmployees;
    }

    public Employee findByPhone(String phone){
        return employeeRepository.findByPhone(phone)
                .orElseThrow(() -> {
                    log.warn("[findByPhone] Employee with Phone {} not found", phone);
                    return new EntityNotFoundException("Employee with phone " + phone + NOT_FOUND_SUFFIX);
                });
    }

    public Employee findByEmail(String email){
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("[findByEmail] Employee with email {} not found", email);
                    return new EntityNotFoundException("Employee with email " + email + NOT_FOUND_SUFFIX);
                });
    }

    public List<Employee> findByRole(EmployeeRole role){
        List<Employee> employeesByRole = employeeRepository.findByRole(role);
        log.debug("[findByRole] Found {} employees(s)", employeesByRole.size());
        return employeesByRole;
    }

    public List<Employee> findAllActive() {
        List<Employee> activeEmployees =  employeeRepository.findByIsActiveIsTrue();
        log.debug("[findAllActive] Found {} employee(s)", activeEmployees.size());
        return activeEmployees;
    }

    public List<Employee> findAllInactive() {
        List<Employee> inactiveEmployees =  employeeRepository.findByIsActiveIsFalse();
        log.debug("[findAllInactive] Found {} employee(s)", inactiveEmployees.size());
        return inactiveEmployees;
    }

    public void deactivate(Long id) {
        Employee employee = findById(id);
        employee.setIsActive(false);
        employeeRepository.save(employee);
        log.info("[deactivate] Employee with id {} has been deactivated", id);

        //TODO: implement messaging trigger
    }
}
