package com.medisync.MediSync.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DoctorRegistrationDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotNull(message = "Appointment duration is required")
    private Integer appointmentDuration;

}
