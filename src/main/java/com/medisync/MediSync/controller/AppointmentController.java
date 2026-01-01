package com.medisync.MediSync.controller;

import com.medisync.MediSync.dto.AppointmentBookDto;
import com.medisync.MediSync.dto.AppointmentDto;
import com.medisync.MediSync.entity.Appointment;
import com.medisync.MediSync.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(appointmentService.findById(appointmentId));
    }

    @PostMapping
    public ResponseEntity<AppointmentDto> bookAppointment(@Valid @RequestBody AppointmentBookDto appointmentDto) {
        return new ResponseEntity<>(appointmentService.bookAppointment(appointmentDto), HttpStatus.CREATED);
    }
}
