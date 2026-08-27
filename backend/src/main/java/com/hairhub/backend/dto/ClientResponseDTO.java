package com.hairhub.backend.dto;

public record ClientResponseDTO(
        Long id,
        String name,
        String phone,
        String email,
        Boolean isActive
) {
}
