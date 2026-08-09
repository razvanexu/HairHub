package com.hairhub.backend.repository;

import com.hairhub.backend.entity.Appointment;
import com.hairhub.backend.entity.Client;
import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.ServiceType;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {
    Appointment save(Appointment appointment);

    Optional<Appointment> findById(Long id);

    List<Appointment> findAll();

    List<Appointment> findByClient(Client client);

    List<Appointment> findByEmployee(Employee employee);

    List<Appointment> findByServiceType(ServiceType serviceType);
}
