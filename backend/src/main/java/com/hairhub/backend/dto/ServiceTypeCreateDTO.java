package com.hairhub.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServiceTypeCreateDTO(
        @NotBlank
        String name,

        @NotNull
        Integer duration
) {
}
