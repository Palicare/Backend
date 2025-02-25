package com.github.palicare.patient;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ProfilePictureService {
    private final Path storagePath = Paths.get("profile-pictures");

    public void storeProfilePicture(Long patientId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        try {
            String filename = patientId + ".jpg";
            Files.createDirectories(storagePath);
            Files.copy(file.getInputStream(), storagePath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }

    public Resource loadProfilePicture(Long patientId) {
        try {
            Path file = storagePath.resolve(patientId + ".jpg");
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Profile picture not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load profile picture", e);
        }
    }
}
