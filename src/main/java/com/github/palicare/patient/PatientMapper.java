package com.github.palicare.patient;

import com.github.palicare.contact.ContactDTO;
import com.github.palicare.contact.ContactEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDTO toPatientDTO(PatientEntity patientEntity);
    ContactDTO toContactDTO(ContactEntity contactEntity);
    PatientEntity toPatientEntity(PatientDTO patientEntity);
    ContactEntity toContactEntity(ContactDTO contactEntity);
}
