package com.medisync.MediSync.dto;

import com.medisync.MediSync.entity.Appointment;
import com.medisync.MediSync.entity.Doctor;
import com.medisync.MediSync.entity.Patient;
import com.medisync.MediSync.entity.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentDto {
    private Long id;
    private LocalDateTime appointmentTime;
    private String reason;
    private AppointmentStatus status;
    private DoctorDto doctor;
    private PatientDto patient;

    public static AppointmentDto mapToDto(Appointment appointment) {
        return AppointmentDto.builder()
                .id(appointment.getId())
                .appointmentTime(appointment.getAppointmentTime())
                .reason(appointment.getReason())
                .status(appointment.getStatus())
                .doctor(DoctorDto.mapToDto(appointment.getDoctor()))
                .patient(PatientDto.mapToDto(appointment.getPatient()))
                .build();
    }
}
