package com.medisync.MediSync.controller;

import com.medisync.MediSync.dto.AppointmentDto;
import com.medisync.MediSync.dto.DoctorDto;
import com.medisync.MediSync.dto.DoctorRegistrationDto;
import com.medisync.MediSync.dto.DoctorUpdateDto;
import com.medisync.MediSync.service.AppointmentService;
import com.medisync.MediSync.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getDoctors(@RequestParam(required = false) Long departmentId,
                                                      @RequestParam(defaultValue = "false") boolean deactivated) {
        return ResponseEntity.ok(doctorService.getDoctors(departmentId, deactivated));
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long doctorId) {
        DoctorDto doctorDto = doctorService.getDoctorById(doctorId);
        return ResponseEntity.ok(doctorDto);
    }

    @GetMapping("/{doctorId}/appointments")
    @PreAuthorize("hasAnyRole('DOCTOR, ADMIN')")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(doctorId));
    }

    @GetMapping("/{doctorId}/appointments/slots")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getAvailableSlots(doctorId, date));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorDto> registerDoctor(@Valid @RequestBody DoctorRegistrationDto doctorRegistrationDto) {
        return new ResponseEntity<>(doctorService.registerDoctor(doctorRegistrationDto), HttpStatus.CREATED);
    }

    @PutMapping("/{doctorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable Long doctorId, @Valid @RequestBody DoctorUpdateDto doctorUpdateDto) {
        return ResponseEntity.ok(doctorService.updateDoctor(doctorId, doctorUpdateDto));
    }

    @PutMapping("/{doctorId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorDto> deactivateDoctor(@PathVariable Long doctorId) {
        doctorService.deactivateDoctor(doctorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{doctorId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorDto> activate(@PathVariable Long doctorId) {
        doctorService.activateDoctor(doctorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
