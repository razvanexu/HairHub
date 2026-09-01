package com.hairhub.backend.service;

import com.hairhub.backend.dto.AppointmentCreateDTO;
import com.hairhub.backend.entity.Appointment;
import com.hairhub.backend.entity.Client;
import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.ServiceType;
import com.hairhub.backend.entity.enums.AppointmentStatus;
import com.hairhub.backend.exceptions.EntityNotFoundException;
import com.hairhub.backend.mapper.AppointmentMapper;
import com.hairhub.backend.repository.AppointmentRepository;
import com.hairhub.backend.repository.ClientRepository;
import com.hairhub.backend.repository.EmployeeRepository;
import com.hairhub.backend.repository.ServiceTypeRepository;
import com.hairhub.backend.service.validators.OverlapValidation;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    ServiceTypeRepository serviceTypeRepository;

    @Mock
    AppointmentMapper appointmentMapper;

    @Mock
    private OverlapValidation validation;

    private AppointmentService appointmentService;

    @BeforeEach
    void setup() {
        appointmentService = new AppointmentService(appointmentRepository,
                clientRepository, employeeRepository, appointmentMapper, serviceTypeRepository, validation);
    }

    @Test
    void create_withNoConflict_savesAndReturnsAppointment() {
        //Given
        Long employeeId = 1L;
        Long serviceTypeId = 1L;
        Employee employee = new Employee("Ion", "0725475548", "ion@mail.de");
        ServiceType tuns = new ServiceType("tuns", 50, 30, null);
        LocalDateTime startTime = LocalDateTime.of(2026, Month.AUGUST, 6, 10, 0);
        AppointmentCreateDTO dto = new AppointmentCreateDTO(null, employeeId, serviceTypeId, startTime);
        Appointment mappedAppointment = new Appointment(null, employee, tuns, startTime, 30, AppointmentStatus.PENDING);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(serviceTypeRepository.findById(serviceTypeId)).thenReturn(Optional.of(tuns));
        when(appointmentMapper.toAppointment(dto, null, employee, tuns)).thenReturn(mappedAppointment);
        when(appointmentRepository.save(mappedAppointment)).thenReturn(mappedAppointment);

        //When
        Appointment result = appointmentService.create(dto);

        //Then
        assertEquals(employee, result.getEmployee());
        assertEquals(startTime, result.getStartTime());
        assertEquals(AppointmentStatus.PENDING, result.getAppointmentStatus());
        verify(appointmentRepository).save(mappedAppointment);
    }

    @Test
    void create_withConflict_throwsValidationException_doesNotSave() {
        //Given
        Long employeeId = 1L;
        Long serviceTypeId = 2L;
        Employee employee = new Employee("Ion", "0725475548", "ion@mail.de");
        ServiceType tuns = new ServiceType("tuns", 50, 30, null);
        LocalDateTime startTime = LocalDateTime.of(2026, Month.AUGUST, 6, 10, 0);
        AppointmentCreateDTO dto = new AppointmentCreateDTO(null, employeeId, serviceTypeId, startTime);
        Appointment mappedAppointment = new Appointment(null, employee, tuns, startTime, 30, AppointmentStatus.PENDING);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(serviceTypeRepository.findById(serviceTypeId)).thenReturn(Optional.of(tuns));
        when(appointmentMapper.toAppointment(dto, null, employee, tuns)).thenReturn(mappedAppointment);
        doThrow(new ValidationException("Overlap"))
                .when(validation).validate(employee, startTime, 30, null);

        //When //Then
        assertThrows(ValidationException.class, () -> appointmentService.create(dto));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void findById_withExistentId_returnsAppointment() {
        //Given
        Long id = 1L;
        Appointment appointment = mock(Appointment.class);
        when(appointment.getId()).thenReturn(id);
        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointment));

        //When
        Appointment result = appointmentService.findById(id);

        //Then
        assertEquals(id, result.getId());
    }

    @Test
    void findById_withNonExistentId_throwsEntityNotFoundException() {
        Long id = 999L;
        when(appointmentRepository.findById(id)).thenReturn(Optional.empty());

        //When //Then
        assertThrows(EntityNotFoundException.class, () -> appointmentService.findById(id));
    }

    @Test
    void findAll_returnsAppointmentList() {
        //Given
        Employee employee = new Employee("Ion", "0725475548", "ion@mail.de");
        LocalDateTime startTime = LocalDateTime.of(2026, Month.AUGUST, 6, 10, 0);
        ServiceType tuns = new ServiceType("tuns", 50, 30, null);
        Appointment appointment = new Appointment(null, employee, tuns, startTime, 25, AppointmentStatus.CONFIRMED);
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

        //When
        List<Appointment> result = appointmentService.findAll();

        //Then
        assertEquals(1, result.size());
        assertEquals(employee, result.get(0).getEmployee());
        assertEquals(startTime, result.get(0).getStartTime());
        assertEquals(tuns, result.get(0).getServiceType());
        assertEquals(AppointmentStatus.CONFIRMED, result.get(0).getAppointmentStatus());
    }

    @Test
    void findByClient_returnsAppointmentList() {
        //Given
        Client client = new Client("Ion", "0726951657", "ion@gn.com");
        Employee employee = new Employee("Ion", "0725475548", "ion@mail.de");
        LocalDateTime startTime = LocalDateTime.of(2026, Month.AUGUST, 6, 10, 0);
        ServiceType tuns = new ServiceType("tuns", 50, 30, null);
        Appointment appointment = new Appointment(client, employee, tuns, startTime, 25, AppointmentStatus.CONFIRMED);
        when(appointmentRepository.findByClient(client)).thenReturn(List.of(appointment));

        //When
        List<Appointment> result = appointmentService.findByClient(client);

        //Then
        assertEquals(1, result.size());
        assertEquals(client, result.get(0).getClient());
    }

    @Test
    void findByEmployee_returnsAppointmentList() {
        //Given
        Client client = new Client("Ion", "0726951657", "ion@gn.com");
        Employee employee = new Employee("Ion", "0725475548", "ion@mail.de");
        LocalDateTime startTime = LocalDateTime.of(2026, Month.AUGUST, 6, 10, 0);
        ServiceType tuns = new ServiceType("tuns", 50, 30, null);
        Appointment appointment = new Appointment(client, employee, tuns, startTime, 25, AppointmentStatus.CONFIRMED);
        when(appointmentRepository.findByEmployee(employee)).thenReturn(List.of(appointment));

        //When
        List<Appointment> result = appointmentService.findByEmployee(employee);

        //Then
        assertEquals(1, result.size());
        assertEquals(employee, result.get(0).getEmployee());
    }

    @Test
    void findByServiceType_returnsAppointmentList() {
        //Given
        Client client = new Client("Ion", "0726951657", "ion@gn.com");
        Employee employee = new Employee("Ion", "0725475548", "ion@mail.de");
        LocalDateTime startTime = LocalDateTime.of(2026, Month.AUGUST, 6, 10, 0);
        ServiceType tuns = new ServiceType("tuns", 50, 30, null);
        Appointment appointment = new Appointment(client, employee, tuns, startTime, 25, AppointmentStatus.CONFIRMED);
        when(appointmentRepository.findByServiceType(tuns)).thenReturn(List.of(appointment));

        //When
        List<Appointment> result = appointmentService.findByServiceType(tuns);

        //Then
        assertEquals(1, result.size());
        assertEquals(tuns, result.get(0).getServiceType());
    }

    @Test
    void cancel_findsAppointmentById_changesStatusCancelled() {
        //Given
        Long id = 1L;
        Employee employee = new Employee("Ion", "0725475548", "ion@mail.de");
        LocalDateTime startTime = LocalDateTime.of(2026, Month.AUGUST, 6, 10, 0);
        ServiceType tuns = new ServiceType("tuns", 50, 30, null);
        Appointment appointment = new Appointment(null, employee, tuns, startTime, 30, AppointmentStatus.CONFIRMED);
        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointment));

        //When
        appointmentService.cancel(id);

        // Then
        assertEquals(AppointmentStatus.CANCELLED, appointment.getAppointmentStatus());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void cancel_withNonExistentId_throwsEntityNotFoundException() {
        //Given
        Long id = 999L;
        when(appointmentRepository.findById(id)).thenReturn(Optional.empty());

        //When //Then
        assertThrows(EntityNotFoundException.class, () -> appointmentService.cancel(id));
        verify(appointmentRepository, never()).save(any());
    }
}
