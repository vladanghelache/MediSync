package com.medisync.MediSync.repository;

import com.medisync.MediSync.entity.Appointment;
import com.medisync.MediSync.entity.Doctor;
import com.medisync.MediSync.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);
}
