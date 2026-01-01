package com.medisync.MediSync.service;

import com.medisync.MediSync.dto.AppointmentBookDto;
import com.medisync.MediSync.dto.AppointmentDto;
import com.medisync.MediSync.entity.Appointment;
import com.medisync.MediSync.entity.Doctor;
import com.medisync.MediSync.entity.DoctorSchedule;
import com.medisync.MediSync.entity.Patient;
import com.medisync.MediSync.entity.enums.AppointmentDuration;
import com.medisync.MediSync.entity.enums.AppointmentStatus;
import com.medisync.MediSync.exception.ResourceNotFoundException;
import com.medisync.MediSync.repository.AppointmentRepository;
import com.medisync.MediSync.repository.DoctorRepository;
import com.medisync.MediSync.repository.DoctorScheduleRepository;
import com.medisync.MediSync.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;

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

    public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {

        if (date.isBefore(LocalDate.now())) {
            return Collections.emptyList();
        }

        DoctorSchedule doctorSchedule = doctorScheduleRepository
                .findByDoctorIdAndDayOfWeek(doctorId, date.getDayOfWeek())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor is not working on " + date.getDayOfWeek()));

        Doctor doctor = doctorSchedule.getDoctor();
        int duration = doctor.getAppointmentDuration().getMinutes();

        LocalDateTime startTime = doctorSchedule.getStartTime().atDate(date);
        LocalDateTime endTime  = doctorSchedule.getEndTime().atDate(date);

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId, startTime, endTime
        );

        Set<LocalTime> bookedSlots = appointments.stream()
                .map(appointment -> appointment.getAppointmentTime().toLocalTime())
                .collect(Collectors.toSet());

        List<LocalTime> availableSlots = new ArrayList<>();

        for (
                LocalTime slot = doctorSchedule.getStartTime();
                slot.plusMinutes(duration).isBefore(doctorSchedule.getEndTime())||
                slot.plusMinutes(duration).equals(doctorSchedule.getEndTime());
                slot = slot.plusMinutes(duration)
        ) {
            if (date.equals(LocalDate.now()) && slot.isBefore(LocalTime.now())) {
                continue;
            }

            if (!bookedSlots.contains(slot)) {
                availableSlots.add(slot);
            }
        }

        return availableSlots;
    }

    @Transactional
    public AppointmentDto bookAppointment(AppointmentBookDto appointmentBookDto){

        Doctor doctor = doctorRepository.findByIdWithLock(appointmentBookDto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id=" + appointmentBookDto.getDoctorId() + " not found."));

        AppointmentDuration appointmentDuration = doctor.getAppointmentDuration();

        LocalDateTime startTime = appointmentBookDto.getAppointmentTime();
        LocalDateTime endTime = appointmentBookDto.getAppointmentTime().plusMinutes(appointmentDuration.getMinutes());

        DayOfWeek dayOfWeek = appointmentBookDto.getAppointmentTime().getDayOfWeek();

        DoctorSchedule doctorSchedule = doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctor.getId(), dayOfWeek)
                .orElseThrow(() -> new IllegalArgumentException("Doctor is not working on " + dayOfWeek));

        if(startTime.toLocalTime().isBefore(doctorSchedule.getStartTime())
                || endTime.toLocalTime().isAfter(doctorSchedule.getEndTime())){
            throw new IllegalArgumentException("Requested time is outside of doctor's working hours (" +
                    doctorSchedule.getStartTime() + " - " + doctorSchedule.getEndTime() + ")");
        }

        long minutesFromStart = java.time.Duration.between(
                doctorSchedule.getStartTime(),
                startTime.toLocalTime()
        ).toMinutes();

        if (minutesFromStart % appointmentDuration.getMinutes() != 0) {
            throw new IllegalArgumentException(
                    "Appointment time is not aligned with doctor's schedule. The schedule starts at " +
                            doctorSchedule.getStartTime() + " with slots of " +
                    appointmentDuration.getMinutes() + " mins). Valid slots are like " +
                    doctorSchedule.getStartTime().plusMinutes(appointmentDuration.getMinutes()));
        }

        boolean notAvailable = appointmentRepository.existsByDoctorAppointmentTime(
                doctor.getId(),
                appointmentBookDto.getAppointmentTime()
        );

        if (notAvailable){
            throw new IllegalStateException("Doctor is already booked for this time slot.");
        }

        Patient patient = patientRepository.findById(appointmentBookDto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id=" + appointmentBookDto.getPatientId() + " not found."));

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
