package com.github.palicare.contact;

import java.time.LocalDate;

public record ContactDTO(Long id, String firstName, String lastName, LocalDate birthDate, boolean power,
                         String relation, String phoneNumber) {
}