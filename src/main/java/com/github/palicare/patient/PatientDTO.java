package com.github.palicare.patient;

import com.github.palicare.contact.ContactDTO;

import java.time.LocalDate;
import java.util.List;

public record PatientDTO(Long id, String firstName, String lastName, LocalDate birthDate, String gender,
                         String religion, String diet, String nationality, byte careLevel,
                         String misc, int roomNumber, ContactDTO contact, List<String> allergies,
                         List<String> careNeeds, List<String> symptoms) {
}