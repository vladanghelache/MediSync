package com.medisync.MediSync.controller;

import com.medisync.MediSync.dto.MedicalRecordCreateDto;
import com.medisync.MediSync.dto.MedicalRecordDto;
import com.medisync.MediSync.repository.MedicalRecordRepository;
import com.medisync.MediSync.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    @PutMapping("/{medicalRecordId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecordDto> updateMedicalRecord(
            @PathVariable Long medicalRecordId,
            @Valid @RequestBody MedicalRecordCreateDto medicalRecordCreateDto
    ) {
        return ResponseEntity.ok(medicalRecordService.updateMedicalRecord(medicalRecordId, medicalRecordCreateDto));
    }

    @DeleteMapping("/{medicalRecordId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long medicalRecordId) {
        medicalRecordService.deleteMedicalRecord(medicalRecordId);
        return ResponseEntity.noContent().build();
    }

}
