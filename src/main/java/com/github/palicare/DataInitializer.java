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
        if (this.patientRepository.count() != 0)
            return;
        PatientDTO patient1 = new PatientDTO(
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
                new ContactDTO(null, "Erika", "Mustermann", LocalDate.now(), true, "Schwester", "01773012485"),
                List.of("Hausstaub", "Heuschnupfen"),
                List.of("Keine"),
                List.of("Fieber", "Husten")
        );
        patientService.savePatient(patient1);

        PatientDTO patient2 = new PatientDTO(
                null,
                "Lisa",
                "Müller",
                LocalDate.of(1985, 6, 15),
                "Weiblich",
                "Atheistisch",
                "Asthma",
                "Deutsch",
                (byte) 2,
                "Zusatzinfo: Allergikerin",
                120,
                new ContactDTO(null, "Hans", "Müller", LocalDate.of(1955, 3, 20), false, "Vater", "01774561234"),
                List.of("Pollen", "Erdnüsse"),
                List.of("Laktoseintoleranz"),
                List.of("Kurzatmigkeit", "Hautausschlag")
        );
        patientService.savePatient(patient2);

        PatientDTO patient3 = new PatientDTO(
                null,
                "Felix",
                "Schneider",
                LocalDate.of(1992, 11, 30),
                "Männlich",
                "Islamisch",
                "Bluthochdruck",
                "Deutsch",
                (byte) 4,
                "Sportler",
                110,
                new ContactDTO(null, "Miriam", "Schneider", LocalDate.of(1990, 8, 10), false, "Ehefrau", "01771234567"),
                List.of("Keine"),
                List.of("Vegetarier"),
                List.of("Kopfschmerzen", "Schwindel")
        );
        patientService.savePatient(patient3);
    }
}
