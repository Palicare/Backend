package com.github.palicare.patient;

import com.github.palicare.contact.ContactEntity;
import com.github.palicare.contact.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
public class PatientController {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final ContactRepository contactRepository;

    @Autowired
    public PatientController(PatientRepository patientRepository, ContactRepository contactRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.contactRepository = contactRepository;
        this.patientMapper = patientMapper;
    }

    @GetMapping(path = "/findAll")
    public List<PatientEntity> findAll() {
        return patientRepository.findAll();
    }

    @GetMapping(path = "/create")
    public PatientDTO create() {
        ContactEntity contact = new ContactEntity();
        contact.setFirstName("Moritz");
        contact.setLastName("Muster");
        contact.setBirthDate(LocalDate.now());
        contact.setPower(false);
        contact.setCare("");
        contact.setRelation("Brother");
        contact = contactRepository.save(contact);

        PatientEntity patient = new PatientEntity();
        patient.setFirstName("Max");
        patient.setLastName("Muster");
        patient.setBirthDate(LocalDate.now());
        patient.setGender("Male");
        patient.setReligion("Christian");
        patient.setDiet("default");
        patient.setNationality("Germany");
        patient.setProfilePicturePath("");
        patient.setCareLevel((byte) 5);
        patient.setMisc("");
        patient.setRoomNumber(0);
        patient.setAllergies(List.of("House dust"));
        patient.setCareNeeds(Collections.emptyList());
        patient.setSymptoms(List.of("Depression"));
        patient.setContact(contact);
        patient = patientRepository.save(patient);
        return patientMapper.toPatientDTO(patient);
    }
}
