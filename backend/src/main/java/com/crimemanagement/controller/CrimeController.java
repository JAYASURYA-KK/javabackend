package com.crimemanagement.controller;

import com.crimemanagement.model.Crime;
import com.crimemanagement.service.CrimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/crimes")
@CrossOrigin(origins = "*")


public class CrimeController {
    
    @Autowired
    private CrimeService crimeService;

    // Define the upload directory
    private static final String UPLOAD_DIR = "uploads"; // This defines "uploads/"

    // Static block to create the upload directory if it doesn't exist
    static {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
                System.out.println("Created upload directory: " + uploadPath.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to create upload directory: " + e.getMessage());
            }
        }
    }
    
    @PostMapping
    public ResponseEntity<Crime> createCrime(
            @RequestParam("crimeType") String crimeType,
            @RequestParam("location") String location,
            @RequestParam("description") String description,
            @RequestParam(value = "reportedBy", defaultValue = "Anonymous") String reportedBy,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {
        
        Crime crime = new Crime();
        crime.setCrimeType(crimeType);
        crime.setLocation(location);
        crime.setDescription(description);
        crime.setReportedBy(reportedBy);
        crime.setDateTime(LocalDateTime.now());
        crime.setStatus("REPORTED");

        if ("Serial Killer".equalsIgnoreCase(crimeType)) {
            crime.setPriority("HIGH");
            crime.setAlertLevel("CRITICAL");
            crime.setSpecialUnit("Criminal Investigation Department");
            crime.setRequiresImmediateAttention(true);
        } else {
            crime.setPriority("MEDIUM");
            crime.setAlertLevel("NORMAL");
            crime.setRequiresImmediateAttention(false);
        }

        List<String> imageUrls = new ArrayList<>();
        if (images != null && images.length > 0) {
            for (MultipartFile image : images) {
                try {
                    String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                    Path filePath = Paths.get(UPLOAD_DIR, fileName);
                    Files.copy(image.getInputStream(), filePath);
                    // THIS LINE IS CRUCIAL: It adds the leading '/' and uses UPLOAD_DIR which has the trailing '/'
                    imageUrls.add("/" + UPLOAD_DIR+"/"+ fileName); // Should result in "/uploads/filename.jpg"
                } catch (IOException e) {
                    System.err.println("Failed to upload image: " + e.getMessage());
                }
            }
        }
        crime.setImages(imageUrls);

        Crime savedCrime = crimeService.saveCrime(crime);
        return ResponseEntity.ok(savedCrime);
    }
    
    @GetMapping
    public ResponseEntity<List<Crime>> getAllCrimes() {
        List<Crime> crimes = crimeService.getAllCrimes();
        return ResponseEntity.ok(crimes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Crime> getCrimeById(@PathVariable String id) {
        Crime crime = crimeService.getCrimeById(id);
        if (crime != null) {
            return ResponseEntity.ok(crime);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/type/{crimeType}")
    public ResponseEntity<List<Crime>> getCrimesByType(@PathVariable String crimeType) {
        List<Crime> crimes = crimeService.getCrimesByType(crimeType);
        return ResponseEntity.ok(crimes);
    }
    
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Crime>> getCrimesByLocation(@PathVariable String location) {
        List<Crime> crimes = crimeService.getCrimesByLocation(location);
        return ResponseEntity.ok(crimes);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Crime>> searchCrimes(@RequestParam String query) {
        List<Crime> crimes = crimeService.searchCrimes(query);
        return ResponseEntity.ok(crimes);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Crime> updateCrime(@PathVariable String id, @RequestBody Crime crime) {
        Crime updatedCrime = crimeService.updateCrime(id, crime);
        if (updatedCrime != null) {
            return ResponseEntity.ok(updatedCrime);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCrime(@PathVariable String id) {
        crimeService.deleteCrime(id);
        return ResponseEntity.noContent().build();
    }
}
