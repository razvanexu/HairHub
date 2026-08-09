package com.hairhub.backend.service;

import com.hairhub.backend.entity.Appointment;
import com.hairhub.backend.entity.Client;
import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.ServiceType;
import com.hairhub.backend.entity.enums.AppointmentStatus;
import com.hairhub.backend.exceptions.EntityNotFoundException;
import com.hairhub.backend.repository.AppointmentRepository;
import com.hairhub.backend.service.validators.OverlapValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AppointmentService {
    private static final String NOT_FOUND_SUFFIX = " not found";
    private final AppointmentRepository appointmentRepository;
    private final OverlapValidation overlapValidation;

    public AppointmentService(AppointmentRepository appointmentRepository, OverlapValidation overlapValidation) {
        this.appointmentRepository = appointmentRepository;
        this.overlapValidation = overlapValidation;
    }

    public Appointment create(Appointment appointment) {
        overlapValidation.validate(appointment.getEmployee(), appointment.getStartTime(),
                appointment.getDuration(), appointment.getId());
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
}
