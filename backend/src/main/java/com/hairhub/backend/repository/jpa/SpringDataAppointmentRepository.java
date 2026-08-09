package com.hairhub.backend.repository.jpa;

import com.hairhub.backend.entity.Appointment;
import com.hairhub.backend.entity.Client;
import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataAppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByClient(Client client);

    List<Appointment> findByEmployee(Employee employee);

    List<Appointment> findByServiceType(ServiceType serviceType);
}
