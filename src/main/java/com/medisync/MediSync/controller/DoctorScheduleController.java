package com.medisync.MediSync.controller;

import com.medisync.MediSync.dto.DoctorScheduleCreateDto;
import com.medisync.MediSync.dto.DoctorScheduleDto;
import com.medisync.MediSync.service.DoctorScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors/{doctorId}/schedules")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;

    @GetMapping
    public ResponseEntity<List<DoctorScheduleDto>> getFullSchedule(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorScheduleService.getSchedules(doctorId));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<DoctorScheduleDto> getSchedule(@PathVariable Long doctorId, @PathVariable Long scheduleId) {
        return ResponseEntity.ok(doctorScheduleService.getSchedule(scheduleId));
    }

    @PostMapping
    public ResponseEntity<DoctorScheduleDto> createSchedule(
            @PathVariable Long doctorId,
            @Valid @RequestBody DoctorScheduleCreateDto doctorScheduleCreateDto) {
        return new ResponseEntity<>(doctorScheduleService.createSchedule(doctorId, doctorScheduleCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<DoctorScheduleDto> updateSchedule(
            @PathVariable Long doctorId,
            @PathVariable Long scheduleId,
            @Valid @RequestBody DoctorScheduleCreateDto doctorScheduleCreateDto) {
        return ResponseEntity.ok(doctorScheduleService.updateSchedule(scheduleId, doctorScheduleCreateDto));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long doctorId,
            @PathVariable Long scheduleId) {
        doctorScheduleService.deleteSchedule(scheduleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}