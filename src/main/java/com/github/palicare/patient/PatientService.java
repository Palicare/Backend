package com.github.palicare.patient;

import com.github.palicare.contact.ContactEntity;
import com.github.palicare.contact.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final ContactRepository contactRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository, ContactRepository contactRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.contactRepository = contactRepository;
        this.patientMapper = patientMapper;
    }

    public List<PatientDTO> findAllPatients() {
        return patientRepository.findAll().stream().map(patientMapper::toPatientDTO).collect(Collectors.toList());
    }

    public PatientDTO findPatientById(Long id) {
        return patientRepository.findById(id).map(patientMapper::toPatientDTO).orElse(null);
    }

    public PatientDTO savePatient(PatientDTO patientDTO) {
        PatientEntity patientEntity = patientMapper.toPatientEntity(patientDTO);
        ContactEntity contactEntity = patientMapper.toContactEntity(patientDTO.contact());
        contactEntity = contactRepository.save(contactEntity);
        patientEntity.setContact(contactEntity);
        patientEntity = patientRepository.save(patientEntity);
        return patientMapper.toPatientDTO(patientEntity);
    }
}
