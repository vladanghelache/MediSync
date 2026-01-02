package com.medisync.MediSync.service;

import com.medisync.MediSync.dto.PatientDto;
import com.medisync.MediSync.dto.PatientRegistrationDto;
import com.medisync.MediSync.dto.PatientUpdateDto;
import com.medisync.MediSync.entity.Allergy;
import com.medisync.MediSync.entity.Patient;
import com.medisync.MediSync.entity.User;
import com.medisync.MediSync.exception.ResourceNotFoundException;
import com.medisync.MediSync.repository.AllergyRepository;
import com.medisync.MediSync.repository.PatientRepository;
import com.medisync.MediSync.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final AllergyRepository allergyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerPatient(PatientRegistrationDto patientRegistrationDto) {
        User user = User.builder()
                .email(patientRegistrationDto.getEmail())
                .password(passwordEncoder.encode(patientRegistrationDto.getPassword()))
                .build();
        userRepository.save(user);

        Patient patient = Patient.builder()
                .firstName(patientRegistrationDto.getFirstName())
                .lastName(patientRegistrationDto.getLastName())
                .gender(patientRegistrationDto.getGender())
                .phoneNumber(patientRegistrationDto.getPhoneNumber())
                .dateOfBirth(patientRegistrationDto.getDateOfBirth())
                .address(patientRegistrationDto.getAddress())
                .city(patientRegistrationDto.getCity())
                .county(patientRegistrationDto.getCounty())
                .country(patientRegistrationDto.getCountry())
                .user(user)
                .build();

        if (
                patientRegistrationDto.getAllergyIds() != null && !patientRegistrationDto.getAllergyIds().isEmpty()
        ) {
            List<Allergy> allergies = allergyRepository.findAllById(patientRegistrationDto.getAllergyIds());
            patient.setAllergies(allergies);
        }

        patientRepository.save(patient);

    }

    public PatientDto getById(Long patientId) {
        return PatientDto.mapToDto(patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id=" + patientId + " not found!")));
    }

    @Transactional
    public PatientDto updatePatient(Long patientId, PatientUpdateDto patientUpdateDto) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new ResourceNotFoundException("Patient with id=" + patientId + " not found!")
        );

        patient.setFirstName(patientUpdateDto.getFirstName());
        patient.setLastName(patientUpdateDto.getLastName());
        patient.setGender(patientUpdateDto.getGender());
        patient.setPhoneNumber(patientUpdateDto.getPhoneNumber());
        patient.setDateOfBirth(patientUpdateDto.getDateOfBirth());
        patient.setAddress(patientUpdateDto.getAddress());
        patient.setCity(patientUpdateDto.getCity());
        patient.setCounty(patientUpdateDto.getCounty());
        patient.setCountry(patientUpdateDto.getCountry());

        if (
                patientUpdateDto.getAllergyIds() != null
        ) {
            List<Allergy> allergies = allergyRepository.findAllById(patientUpdateDto.getAllergyIds());
            patient.getAllergies().clear();
            patient.getAllergies().addAll(allergies);
        }

        patientRepository.save(patient);
        return PatientDto.mapToDto(patient);
    }
}
