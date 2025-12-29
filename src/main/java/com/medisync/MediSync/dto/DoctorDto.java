package com.medisync.MediSync.dto;

import lombok.Data;

@Data
public class DoctorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private String specializationLabel;
    private String departmentId;
    private String departmentName;
}
