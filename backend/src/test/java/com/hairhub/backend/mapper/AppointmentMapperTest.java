package com.hairhub.backend.mapper;

import com.hairhub.backend.dto.AppointmentCreateDTO;
import com.hairhub.backend.dto.AppointmentResponseDTO;
import com.hairhub.backend.entity.Appointment;
import com.hairhub.backend.entity.Client;
import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.ServiceType;
import com.hairhub.backend.entity.enums.AppointmentStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppointmentMapperTest {
    private final AppointmentMapper mapper = new AppointmentMapper();

    @Test
    void toAppointment_mapAllFieldsCorrectly() {
        //Given
        Client client = mock(Client.class);
        when(client.getId()).thenReturn(1L);

        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(1L);

        ServiceType serviceType = mock(ServiceType.class);
        when(serviceType.getId()).thenReturn(1L);

        LocalDateTime startTime = LocalDateTime.of(2026, Month.AUGUST, 6, 10, 0);

        AppointmentCreateDTO createDTO = new AppointmentCreateDTO(1L, 1L, 1L,
                startTime);

        //When
        Appointment result = mapper.toAppointment(createDTO, client, employee, serviceType);

        //Then
        assertNull(result.getId());
        assertEquals(1L, result.getClient().getId());
        assertEquals(1L, result.getEmployee().getId());
        assertEquals(1L, result.getServiceType().getId());
        assertEquals(LocalDateTime.of(2026, Month.AUGUST, 6, 10, 0), result.getStartTime());
        assertEquals(AppointmentStatus.PENDING, result.getAppointmentStatus());
    }

    @Test
    void toAppointmentDTO_mapAllFieldsCorrectly() {
        //Given
        Client client = mock(Client.class);
        when(client.getId()).thenReturn(1L);
        Employee employee = mock(Employee.class);
        when(employee.getId()).thenReturn(1L);
        ServiceType serviceType = mock(ServiceType.class);
        when(serviceType.getId()).thenReturn(1L);
        LocalDateTime start = LocalDateTime.of(2026, Month.AUGUST, 6, 10, 0);
        Appointment appointment = new Appointment(client, employee, serviceType, start, 30, AppointmentStatus.PENDING);

        //When
        AppointmentResponseDTO result = mapper.toAppointmentDTO(appointment);

        //Then
        assertNull(result.id());
        assertEquals(1L, result.clientId());
        assertEquals(1L, result.employeeId());
        assertEquals(1L, result.serviceTypeId());
        assertEquals(LocalDateTime.of(2026, Month.AUGUST, 6, 10, 0), result.startTime());
        assertEquals(30, result.duration());
        assertEquals(AppointmentStatus.PENDING, result.appointmentStatus());
    }
}
