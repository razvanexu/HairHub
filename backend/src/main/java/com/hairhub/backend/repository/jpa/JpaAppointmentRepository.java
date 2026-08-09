package com.hairhub.backend.repository.jpa;

import com.hairhub.backend.entity.Appointment;
import com.hairhub.backend.entity.Client;
import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.ServiceType;
import com.hairhub.backend.repository.AppointmentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaAppointmentRepository implements AppointmentRepository {
    private final SpringDataAppointmentRepository springDataAppointmentRepository;

    public JpaAppointmentRepository(SpringDataAppointmentRepository springDataAppointmentRepository) {
        this.springDataAppointmentRepository = springDataAppointmentRepository;
    }

    @Override
    public Appointment save(Appointment appointment) {
        return springDataAppointmentRepository.save(appointment);
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return springDataAppointmentRepository.findById(id);
    }

    @Override
    public List<Appointment> findAll() {
        return springDataAppointmentRepository.findAll();
    }

    @Override
    public List<Appointment> findByClient(Client client) {
        return springDataAppointmentRepository.findByClient(client);
    }

    @Override
    public List<Appointment> findByEmployee(Employee employee) {
        return springDataAppointmentRepository.findByEmployee(employee);
    }

    @Override
    public List<Appointment> findByServiceType(ServiceType serviceType) {
        return springDataAppointmentRepository.findByServiceType(serviceType);
    }
}
