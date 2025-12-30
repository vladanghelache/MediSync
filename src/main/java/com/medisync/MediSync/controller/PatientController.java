package com.medisync.MediSync.controller;

import com.medisync.MediSync.dto.PatientRegistrationDto;
import com.medisync.MediSync.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<String> registerPatient(@Valid @RequestBody PatientRegistrationDto patientRegistrationDto) {
        patientService.registerPatient(patientRegistrationDto);

        return new ResponseEntity<>("Patient Registration Successful", HttpStatus.CREATED);
    }
}
