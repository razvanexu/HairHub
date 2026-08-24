package com.hairhub.backend.controller;

import com.hairhub.backend.dto.AppointmentCreateDTO;
import com.hairhub.backend.dto.AppointmentResponseDTO;
import com.hairhub.backend.entity.Appointment;
import com.hairhub.backend.mapper.AppointmentMapper;
import com.hairhub.backend.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    public AppointmentController(AppointmentService appointmentService, AppointmentMapper appointmentMapper) {
        this.appointmentService = appointmentService;
        this.appointmentMapper = appointmentMapper;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> postAppointment(@Valid @RequestBody AppointmentCreateDTO dto) {
        Appointment saved = appointmentService.create(dto);
        return ResponseEntity.status(201).body(appointmentMapper.toAppointmentDTO(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable Long id) {
        Appointment appointment = appointmentService.findById(id);
        AppointmentResponseDTO responseDTO = appointmentMapper.toAppointmentDTO(appointment);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointments(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long serviceTypeId) {
        List<AppointmentResponseDTO> appointmentsList = appointmentService
                .search(employeeId, clientId, serviceTypeId)
                .stream()
                .map(appointmentMapper::toAppointmentDTO)
                .toList();
        return ResponseEntity.ok(appointmentsList);
    }


}
