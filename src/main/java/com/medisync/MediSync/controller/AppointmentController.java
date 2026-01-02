package com.medisync.MediSync.controller;

import com.medisync.MediSync.dto.AppointmentBookDto;
import com.medisync.MediSync.dto.AppointmentDto;
import com.medisync.MediSync.dto.MedicalRecordCreateDto;
import com.medisync.MediSync.dto.MedicalRecordDto;
import com.medisync.MediSync.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{appointmentId}/complete")
    public ResponseEntity<MedicalRecordDto> completeAppointment(
            @Valid @RequestBody MedicalRecordCreateDto medicalRecordCreateDto,
            @PathVariable Long appointmentId
    ) {
        return ResponseEntity.ok(appointmentService.completeAppointment(appointmentId,medicalRecordCreateDto));
    }

    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<AppointmentDto> cancelAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentId));
    }

    @PutMapping("/{appointmentId}/no-show")
    public ResponseEntity<AppointmentDto> markNoShow(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(appointmentService.markNoShow(appointmentId));
    }

}
