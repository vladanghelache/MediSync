package com.medisync.MediSync.service;

import com.medisync.MediSync.dto.DoctorDto;
import com.medisync.MediSync.dto.DoctorRegistrationDto;
import com.medisync.MediSync.dto.DoctorUpdateDto;
import com.medisync.MediSync.entity.Department;
import com.medisync.MediSync.entity.Doctor;
import com.medisync.MediSync.entity.User;
import com.medisync.MediSync.entity.enums.AppointmentDuration;
import com.medisync.MediSync.entity.enums.Role;
import com.medisync.MediSync.entity.enums.Specialization;
import com.medisync.MediSync.exception.ResourceNotFoundException;
import com.medisync.MediSync.repository.DepartmentRepository;
import com.medisync.MediSync.repository.DoctorRepository;
import com.medisync.MediSync.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id " + id + " not found"));

        return DoctorDto.mapToDto(doctor);
    }

    public List<DoctorDto> getDoctorByDepartmentId(Long departmentId) {
        if(!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department with id " + departmentId + " not found");
        }
        return doctorRepository.findByDepartmentId(departmentId).stream().map(DoctorDto::mapToDto).toList();
    }

    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream().map(DoctorDto::mapToDto).toList();
    }

    @Transactional
    public  DoctorDto registerDoctor(DoctorRegistrationDto doctorRegistrationDto) {

        if (userRepository.existsByEmail(doctorRegistrationDto.getEmail())){
            throw new IllegalStateException("There is already an account associated with this email: " + doctorRegistrationDto.getEmail());
        }

        User user = User.builder()
                .email(doctorRegistrationDto.getEmail())
                .password(passwordEncoder.encode(doctorRegistrationDto.getPassword()))
                .role(Role.ROLE_DOCTOR)
                .build();

        user = userRepository.save(user);

        Department department = departmentRepository.findById(doctorRegistrationDto.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Department with id=" + doctorRegistrationDto.getDepartmentId() + " not found"
                ));

        Doctor doctor = Doctor.builder()
                .firstName(doctorRegistrationDto.getFirstName())
                .lastName(doctorRegistrationDto.getLastName())
                .specialization(Specialization.valueOf(doctorRegistrationDto.getSpecialization().toUpperCase()))
                .appointmentDuration(AppointmentDuration.valueOf(doctorRegistrationDto.getAppointmentDuration().toUpperCase()))
                .user(user)
                .department(department)
                .build();

        return DoctorDto.mapToDto(doctorRepository.save(doctor));
    }

    @Transactional
    public DoctorDto updateDoctor(Long doctorId, DoctorUpdateDto doctorUpdateDto){
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id " + doctorId + " not found"));

        Department department = departmentRepository.findById(doctorUpdateDto.getDepartmentId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Department with id=" + doctorUpdateDto.getDepartmentId() + " not found"
                        ));

        doctor.setFirstName(doctorUpdateDto.getFirstName());
        doctor.setLastName(doctorUpdateDto.getLastName());
        doctor.setSpecialization(Specialization.valueOf(doctorUpdateDto.getSpecialization().toUpperCase()));
        doctor.setAppointmentDuration(AppointmentDuration.valueOf(doctorUpdateDto.getAppointmentDuration().toUpperCase()));
        doctor.setDepartment(department);

        return DoctorDto.mapToDto(doctorRepository.save(doctor));
    }
}
