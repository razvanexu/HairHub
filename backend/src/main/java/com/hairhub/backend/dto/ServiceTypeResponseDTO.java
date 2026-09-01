package com.hairhub.backend.dto;

public record ServiceTypeResponseDTO(
        Long id,

        String name,

        Integer price,

        Integer duration,

        String description
) {
}
