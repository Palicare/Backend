package com.github.palicare.patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPatientRepository {
    Page<PatientEntity> searchPatients(String query, Pageable pageable);
}