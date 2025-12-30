package com.medisync.MediSync.controller;

import com.medisync.MediSync.dto.DoctorDto;
import com.medisync.MediSync.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getDoctors(@RequestParam(required = false) Long departmentId) {
        if  (departmentId == null) {
            return ResponseEntity.ok(doctorService.getAllDoctors());
        }
        List<DoctorDto> doctors = doctorService.getDoctorByDepartmentId(departmentId);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long doctorId) {
        DoctorDto doctorDto = doctorService.getDoctorById(doctorId);
        return ResponseEntity.ok(doctorDto);
    }
}
