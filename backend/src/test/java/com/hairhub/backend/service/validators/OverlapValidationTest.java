package com.hairhub.backend.service.validators;

import com.hairhub.backend.entity.Appointment;
import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.enums.AppointmentStatus;
import com.hairhub.backend.repository.AppointmentRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverlapValidationTest {
    @Mock
    private AppointmentRepository repo;
    private OverlapValidation validator;

    @BeforeEach
    void setup() {
        validator = new OverlapValidation(repo);
    }

    @Test
    void validate_noExistingAppointments_doesNotThrow() {
        //Given
        Employee employee = new Employee("Ion", "0725475548", "ion@mail.de");
        when(repo.findByEmployee(employee)).thenReturn(List.of());

        //When //Then
        assertDoesNotThrow(() -> validator.validate(employee, LocalDateTime.now(), 30, null));
    }

    @Test
    void validate_overlappingInterval_throwsValidationException() {
        //Given
        Employee employee = new Employee("Ion", "0725475548", "ion@mail.de");
        LocalDateTime startTime = LocalDateTime.of(2026, Month.AUGUST, 6, 10, 0);

        Appointment appointment = mock(Appointment.class);
        when(appointment.getId()).thenReturn(1L);
        when(appointment.getStartTime()).thenReturn(startTime);
        when(appointment.getDuration()).thenReturn(30);
        when(appointment.getAppointmentStatus()).thenReturn(AppointmentStatus.CONFIRMED);
        when(repo.findByEmployee(employee)).thenReturn(List.of(appointment));

        //When //Then
        assertThrows(ValidationException.class, () -> validator.validate(employee, startTime,
                30, null));
    }

    @Test
    void validate_adjacentInterval_doesNotThrow() {
        //Given
        Employee employee = new Employee("Ion", "0725475548", "ion@mail.de");
        LocalDateTime existingStart = LocalDateTime.of(2026, Month.AUGUST, 6, 9, 0);
        LocalDateTime newStart = LocalDateTime.of(2026, Month.AUGUST, 6, 9, 30); // exact cand se termina cealalta

        Appointment appointment = mock(Appointment.class);
        when(appointment.getId()).thenReturn(1L);
        when(appointment.getStartTime()).thenReturn(existingStart);
        when(appointment.getDuration()).thenReturn(30);
        when(appointment.getAppointmentStatus()).thenReturn(AppointmentStatus.CONFIRMED);
        when(repo.findByEmployee(employee)).thenReturn(List.of(appointment));

        //When //Then
        assertDoesNotThrow(() -> validator.validate(employee, newStart, 30, null));
    }

    @Test
    void validate_overlapsWithCancelledAppointment_doesNotThrow() {
        //Given
        Employee employee = new Employee("Ion", "0725475548", "ion@mail.de");
        LocalDateTime startTime = LocalDateTime.of(2026, Month.AUGUST, 6, 10, 0);

        Appointment appointment = mock(Appointment.class);
        when(appointment.getAppointmentStatus()).thenReturn(AppointmentStatus.CANCELLED);
        when(repo.findByEmployee(employee)).thenReturn(List.of(appointment));

        //When //Then
        assertDoesNotThrow(() -> validator.validate(employee, startTime, 30, null));
    }

    @Test
    void validate_excludesOwnAppointmentById_doesNotThrow() {
        //Given
        Employee employee = new Employee("Ion", "0725475548", "ion@mail.de");
        LocalDateTime startTime = LocalDateTime.of(2026, Month.AUGUST, 6, 10, 0);

        Appointment appointment = mock(Appointment.class);
        when(appointment.getId()).thenReturn(1L);
        when(appointment.getAppointmentStatus()).thenReturn(AppointmentStatus.CONFIRMED);
        when(repo.findByEmployee(employee)).thenReturn(List.of(appointment));

        //When //Then
        assertDoesNotThrow(() -> validator.validate(employee, startTime, 30, 1L));
    }
}
