package com.medisync.MediSync.controller;

import com.medisync.MediSync.dto.AppointmentDto;
import com.medisync.MediSync.dto.PatientDto;
import com.medisync.MediSync.dto.PatientRegistrationDto;
import com.medisync.MediSync.dto.PatientUpdateDto;
import com.medisync.MediSync.service.AppointmentService;
import com.medisync.MediSync.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<String> registerPatient(@Valid @RequestBody PatientRegistrationDto patientRegistrationDto) {
        patientService.registerPatient(patientRegistrationDto);

        return new ResponseEntity<>("Patient Registration Successful", HttpStatus.CREATED);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getById(patientId));
    }

    @PutMapping("/{patientId}/")
    public ResponseEntity<PatientDto> updatePatient(
            @PathVariable Long patientId,
            @Valid @RequestBody PatientUpdateDto patientUpdateDto) {
        return ResponseEntity.ok(patientService.updatePatient(patientId, patientUpdateDto));
    }

    @GetMapping("/{patientId}/appointments")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getPatientAppointments(patientId));
    }
}
