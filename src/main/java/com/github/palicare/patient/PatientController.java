package com.github.palicare.patient;

import com.github.palicare.contact.ContactEntity;
import com.github.palicare.contact.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RequestMapping("/patients")
@RestController
public class PatientController {
    private final PatientService patientService;
    private final ProfilePictureService profilePictureService;

    @Autowired
    public PatientController(PatientService patientService, ProfilePictureService profilePictureService) {
        this.patientService = patientService;
        this.profilePictureService = profilePictureService;
    }

    @GetMapping
    public List<PatientDTO> findAllPatients() {
        return patientService.findAllPatients();
    }

    @GetMapping("/{id}")
    public PatientDTO findPatientById(@PathVariable Long id) {
        return patientService.findPatientById(id);
    }

    @PostMapping
    public PatientDTO createPatient(@RequestBody PatientDTO patientDTO) {
        return patientService.savePatient(patientDTO);
    }

    @PostMapping("/{id}/profile-picture")
    public void uploadProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        profilePictureService.storeProfilePicture(id, file);
    }

    @GetMapping("/{id}/profile-picture")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable Long id) {
        Resource file = profilePictureService.loadProfilePicture(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(file);
    }
}
