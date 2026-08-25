package com.hairhub.backend.dto;

import com.hairhub.backend.entity.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentResponseDTO(
        Long id,

        Long clientId,

        @NotNull
        Long employeeId,

        @NotNull
        Long serviceTypeId,

        @NotNull
        LocalDateTime startTime,

        @NotNull
        Integer duration,

        @NotNull
        AppointmentStatus appointmentStatus
) {
}
