package com.hairhub.backend.dto;

import com.hairhub.backend.entity.enums.EmployeeRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmployeeCreateDTO(
        @NotBlank
        String name,

        @NotBlank
        String phone,

        @NotBlank
        @Email
        String email,

        @NotNull
        EmployeeRole role

) {
}
