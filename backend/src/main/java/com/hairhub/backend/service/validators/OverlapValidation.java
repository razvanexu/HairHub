package com.hairhub.backend.service.validators;

import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.enums.AppointmentStatus;
import com.hairhub.backend.repository.AppointmentRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class OverlapValidation {
    private final AppointmentRepository appointmentRepository;

    public OverlapValidation(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public void validate(Employee employee, LocalDateTime startTime, Integer duration, Long excludeAppointmentId) {
        LocalDateTime newEnd = startTime.plusMinutes(duration);

        boolean overlaps = appointmentRepository.findByEmployee(employee).stream()
                .filter(a -> a.getAppointmentStatus() != AppointmentStatus.CANCELLED)
                .filter(a -> !Objects.equals(a.getId(), excludeAppointmentId))
                .anyMatch(a -> startTime.isBefore(a.getStartTime().plusMinutes(a.getDuration()))
                        && newEnd.isAfter(a.getStartTime()));
        if (overlaps) {
            throw new ValidationException("Appointment overlaps with an existing appointment for this employee");
        }
    }
}
