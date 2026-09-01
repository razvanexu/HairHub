package com.hairhub.backend.dto;

public record EmployeeResponseDTO(
        Long id,
        String name,
        String phone,
        String email,
        Boolean isActive
) {
}
