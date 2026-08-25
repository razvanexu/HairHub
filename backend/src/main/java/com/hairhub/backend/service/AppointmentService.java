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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AppointmentService {
    private static final String NOT_FOUND_SUFFIX = " not found";
    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final AppointmentMapper mapper;
    private final ServiceTypeRepository serviceTypeRepository;
    private final OverlapValidation overlapValidation;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              ClientRepository clientRepository,
                              EmployeeRepository employeeRepository, AppointmentMapper mapper,
                              ServiceTypeRepository serviceTypeRepository,
                              OverlapValidation overlapValidation) {
        this.appointmentRepository = appointmentRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
        this.serviceTypeRepository = serviceTypeRepository;
        this.overlapValidation = overlapValidation;
    }

    public Appointment create(AppointmentCreateDTO dto) {
        Client client = dto.clientId() != null ? clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Client with id " + dto.clientId() + NOT_FOUND_SUFFIX))
                : null;
        Employee employee = employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee with id " + dto.employeeId() + NOT_FOUND_SUFFIX));
        ServiceType serviceType = serviceTypeRepository.findById(dto.serviceTypeId())
                .orElseThrow(() -> new EntityNotFoundException("ServiceType with id " + dto.serviceTypeId() + NOT_FOUND_SUFFIX));
        Appointment appointment = mapper.toAppointment(dto, client, employee, serviceType);
        overlapValidation.validate(employee, appointment.getStartTime(),
                appointment.getDuration(), null);
        Appointment saved = appointmentRepository.save(appointment);
        log.info("[create] Appointment with id {} has been created", saved.getId());
        return saved;
    }

    public Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[findById] Appointment with id {} not found", id);
                    return new EntityNotFoundException("Appointment with id " + id + NOT_FOUND_SUFFIX);
                });
    }

    public List<Appointment> findAll() {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        log.debug("[findAll] Found {} appointment(s)", allAppointments.size());
        return allAppointments;
    }

    public List<Appointment> findByClient(Client client) {
        List<Appointment> appointmentByClient = appointmentRepository.findByClient(client);
        log.debug("[findByClient] Found {} appointment(s)", appointmentByClient.size());
        return appointmentByClient;
    }

    public List<Appointment> findByEmployee(Employee employee) {
        List<Appointment> appointmentsByEmployee = appointmentRepository.findByEmployee(employee);
        log.debug("[findByEmployee] Found {} appointment(s)", appointmentsByEmployee.size());
        return appointmentsByEmployee;
    }

    public List<Appointment> findByServiceType(ServiceType serviceType) {
        List<Appointment> appointmentsByServiceType = appointmentRepository.findByServiceType(serviceType);
        log.debug("[findByServiceType] Found {} appointment(s)", appointmentsByServiceType.size());
        return appointmentsByServiceType;
    }

    public void cancel(Long id) {
        Appointment appointment = findById(id);
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        log.info("[cancel] Appointment with id {} has been cancelled", id);
    }

    public List<Appointment> search(Long employeeId, Long clientId, Long serviceTypeId) {
        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EntityNotFoundException("Employee with id " + employeeId + NOT_FOUND_SUFFIX));
            return findByEmployee(employee);
        }
        if (clientId != null) {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new EntityNotFoundException("Client with id " + clientId + NOT_FOUND_SUFFIX));
            return findByClient(client);
        }
        if (serviceTypeId != null) {
            ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
                    .orElseThrow(() -> new EntityNotFoundException("Service type with id " + serviceTypeId + NOT_FOUND_SUFFIX));
            return findByServiceType(serviceType);
        }
        return findAll();
    }
}
