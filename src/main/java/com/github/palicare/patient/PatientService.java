package com.github.palicare.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Autowired
    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    public Page<PatientDTO> searchEmployees(String query, Pageable pageable) {
        return patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                query, query, pageable
        ).map(patientMapper::toPatientDTO);
    }

    public Page<PatientDTO> findAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable).map(patientMapper::toPatientDTO);
    }

    public PatientDTO findPatientById(Long id) {
        return patientRepository.findById(id).map(patientMapper::toPatientDTO).orElse(null);
    }

    public PatientDTO savePatient(PatientDTO patientDTO) {
        PatientEntity patientEntity = patientMapper.toPatientEntity(patientDTO);
        patientEntity = patientRepository.save(patientEntity);
        return patientMapper.toPatientDTO(patientEntity);
    }
}
