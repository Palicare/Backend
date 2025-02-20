package com.github.palicare.contact;

import java.time.LocalDate;
import java.util.List;

public record ContactDTO(Long id, String firstName, String lastName, LocalDate birthDate, boolean power, String care,
                         String relation) {
}