package com.github.palicare.patient;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientEntity, Long>, CustomPatientRepository {

}
