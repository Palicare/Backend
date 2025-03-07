package com.github.palicare;

import com.github.palicare.contact.ContactDTO;
import com.github.palicare.patient.PatientDTO;
import com.github.palicare.patient.PatientRepository;
import com.github.palicare.patient.PatientService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataInitializer {
    private final PatientService patientService;
    private final PatientRepository patientRepository;

    public DataInitializer(PatientService patientService, PatientRepository patientRepository) {
        this.patientService = patientService;
        this.patientRepository = patientRepository;
    }

    @PostConstruct
    public void initData() {
        if (this.patientRepository.count() == 0) {
            for (int i = 0; i < 100; i++) {
                PatientDTO patient = new PatientDTO(
                        null,
                        "Max",
                        "Mustermann",
                        LocalDate.now(),
                        "Männlich",
                        "Christlich",
                        "Keine",
                        "Deutsch",
                        (byte) 3,
                        "Test Zusatz",
                        100,
                        new ContactDTO(null, "Erika", "Mustermann", LocalDate.now(), true, "Schwester"),
                        List.of("Hausstaub", "Heuschnupfen "),
                        List.of("Keine"),
                        List.of("Fieber", "Husten")
                );
                patientService.savePatient(patient);
            }
        }
    }
}
