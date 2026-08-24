package com.hairhub.backend.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentCreateDTO(
        Long clientId,

        @NotNull
        Long employeeId,

        @NotNull
        Long serviceTypeId,

        @NotNull
        LocalDateTime startTime,

        @NotNull
        Integer duration
) {
}
