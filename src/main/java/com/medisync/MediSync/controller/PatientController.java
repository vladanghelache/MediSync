package com.medisync.MediSync.controller;

import com.medisync.MediSync.dto.AppointmentDto;
import com.medisync.MediSync.dto.PatientDto;
import com.medisync.MediSync.dto.PatientRegistrationDto;
import com.medisync.MediSync.dto.PatientUpdateDto;
import com.medisync.MediSync.service.AppointmentService;
import com.medisync.MediSync.service.PatientService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @PostMapping
    @SecurityRequirements()
    public ResponseEntity<String> registerPatient(@Valid @RequestBody PatientRegistrationDto patientRegistrationDto) {
        patientService.registerPatient(patientRegistrationDto);

        return new ResponseEntity<>("Patient Registration Successful", HttpStatus.CREATED);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getById(patientId));
    }

    @PutMapping("/{patientId}/")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PatientDto> updatePatient(
            @PathVariable Long patientId,
            @Valid @RequestBody PatientUpdateDto patientUpdateDto,
            Principal principal) {
        return ResponseEntity.ok(patientService.updatePatient(patientId, patientUpdateDto, principal.getName()));
    }

    @GetMapping("/{patientId}/appointments")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByPatientId(@PathVariable Long patientId,
                                                                           Principal principal) {
        return ResponseEntity.ok(appointmentService.getPatientAppointments(patientId, principal.getName()));
    }

    @PutMapping("/{patientId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivatePatient(@PathVariable Long patientId) {
        patientService.deactivatePatient(patientId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{patientId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activatePatient(@PathVariable Long patientId) {
        patientService.activatePatient(patientId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
