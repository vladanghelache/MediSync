package com.medisync.MediSync.service;

import com.medisync.MediSync.dto.AppointmentBookDto;
import com.medisync.MediSync.dto.AppointmentDto;
import com.medisync.MediSync.entity.Appointment;
import com.medisync.MediSync.entity.Doctor;
import com.medisync.MediSync.entity.Patient;
import com.medisync.MediSync.entity.enums.AppointmentDuration;
import com.medisync.MediSync.entity.enums.AppointmentStatus;
import com.medisync.MediSync.exception.ResourceNotFoundException;
import com.medisync.MediSync.repository.AppointmentRepository;
import com.medisync.MediSync.repository.DoctorRepository;
import com.medisync.MediSync.repository.PatientRepository;
import jakarta.servlet.Servlet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final Servlet servlet;

    public AppointmentDto findById(Long id) {
        return AppointmentDto.mapToDto(appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with id=" + id + " not found.")));
    }

    public List<AppointmentDto> getPatientAppointments(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id=" + patientId + " not found."));

        List<Appointment> appointments = appointmentRepository.findByPatient(patient);
        return appointments.stream().map(AppointmentDto::mapToDto).toList();
    }

    public List<AppointmentDto> getDoctorAppointments(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id=" + doctorId + " not found."));

        List<Appointment> appointments = appointmentRepository.findByDoctor(doctor);
        return appointments.stream().map(AppointmentDto::mapToDto).toList();
    }

    @Transactional
    public AppointmentDto bookAppointment(AppointmentBookDto appointmentBookDto){

        Patient patient = patientRepository.findById(appointmentBookDto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id=" + appointmentBookDto.getPatientId() + " not found."));

        Doctor doctor = doctorRepository.findById(appointmentBookDto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id=" + appointmentBookDto.getDoctorId() + " not found."));

        Appointment appointment = Appointment.builder()
                .appointmentTime(appointmentBookDto.getAppointmentTime())
                .reason(appointmentBookDto.getReason())
                .status(AppointmentStatus.SCHEDULED)
                .doctor(doctor)
                .patient(patient)
                .build();

        return AppointmentDto.mapToDto(appointmentRepository.save(appointment));
    }
}
