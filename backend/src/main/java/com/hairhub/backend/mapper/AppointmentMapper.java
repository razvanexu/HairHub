package com.hairhub.backend.mapper;

import com.hairhub.backend.dto.AppointmentCreateDTO;
import com.hairhub.backend.dto.AppointmentResponseDTO;
import com.hairhub.backend.entity.Appointment;
import com.hairhub.backend.entity.Client;
import com.hairhub.backend.entity.Employee;
import com.hairhub.backend.entity.ServiceType;
import com.hairhub.backend.entity.enums.AppointmentStatus;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public Appointment toAppointment(AppointmentCreateDTO dto, Client client, Employee employee, ServiceType serviceType) {
        return new Appointment(client, employee, serviceType,
                dto.startTime(), dto.duration(), AppointmentStatus.PENDING);
    }

    public AppointmentResponseDTO toAppointmentDTO(Appointment appointment) {
        Long clientId = appointment.getClient() != null ? appointment.getClient().getId() : null;
        return new AppointmentResponseDTO(appointment.getId(), clientId, appointment.getEmployee().getId(),
                appointment.getServiceType().getId(), appointment.getStartTime(),
                appointment.getDuration(), appointment.getAppointmentStatus());
    }
}
