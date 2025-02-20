package com.github.palicare.patient;

import com.github.palicare.contact.ContactDTO;
import com.github.palicare.contact.ContactEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PatientMapper {
    PatientDTO toPatientDTO(PatientEntity patientEntity);
    ContactDTO toContactDTO(ContactEntity contactEntity);
}
