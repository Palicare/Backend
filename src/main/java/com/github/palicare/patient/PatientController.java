package com.github.palicare.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Page<PatientDTO> findAllPatients(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return patientService.findAllPatients(PageRequest.of(page, size));
    }

    @GetMapping("/search")
    public Page<PatientDTO> searchPatients(@RequestParam(defaultValue = "") String query, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return patientService.searchEmployees(query, PageRequest.of(page, size));
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
