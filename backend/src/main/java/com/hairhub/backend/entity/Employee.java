package com.hairhub.backend.entity;

import com.hairhub.backend.entity.enums.EmployeeRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    @NotBlank
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private EmployeeRole role;

    @Column(nullable = false)
    private Boolean isActive;

    public Employee(){}

    public Employee(String name, String phone, String email, EmployeeRole role){
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.isActive = true;
    }

    public Long getId(){ return id; }

    public void setId(Long id){ this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
