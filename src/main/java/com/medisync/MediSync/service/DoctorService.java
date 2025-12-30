package com.medisync.MediSync.service;

import com.medisync.MediSync.dto.DoctorDto;
import com.medisync.MediSync.entity.Doctor;
import com.medisync.MediSync.exception.ResourceNotFoundException;
import com.medisync.MediSync.repository.DepartmentRepository;
import com.medisync.MediSync.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

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
}
