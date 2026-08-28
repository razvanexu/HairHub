package com.hairhub.backend.dto;

import com.hairhub.backend.entity.enums.EmployeeRole;

public record EmployeeResponseDTO(
        Long id,
        String name,
        String phone,
        String email,
        EmployeeRole role,
        Boolean isActive
) {
}
