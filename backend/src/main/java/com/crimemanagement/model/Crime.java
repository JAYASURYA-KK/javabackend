package com.crimemanagement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList; // Import ArrayList

@Document(collection = "crimes")
public class Crime {
    @Id
    private String id;
    private String crimeType;
    private String location;
    private String description;
    private LocalDateTime dateTime;
    private String status;
    private List<String> suspects;
    private String reportedBy;
    private List<String> images; // New field for image URLs
    private String priority; // For Serial Killer cases
    private String alertLevel; // For Serial Killer cases
    private String specialUnit; // For Serial Killer cases
    private boolean requiresImmediateAttention; // For Serial Killer cases
    
    // Constructors
    public Crime() {
        this.images = new ArrayList<>(); // Initialize images list
    }
    
    public Crime(String crimeType, String location, String description, 
                 String reportedBy) {
        this.crimeType = crimeType;
        this.location = location;
        this.description = description;
        this.reportedBy = reportedBy;
        this.dateTime = LocalDateTime.now();
        this.status = "REPORTED";
        this.images = new ArrayList<>(); // Initialize images list
        this.priority = "MEDIUM"; // Default priority
        this.alertLevel = "NORMAL"; // Default alert level
        this.requiresImmediateAttention = false; // Default
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getCrimeType() { return crimeType; }
    public void setCrimeType(String crimeType) { this.crimeType = crimeType; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<String> getSuspects() { return suspects; }
    public void setSuspects(List<String> suspects) { this.suspects = suspects; }
    
    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }

    public String getSpecialUnit() { return specialUnit; }
    public void setSpecialUnit(String specialUnit) { this.specialUnit = specialUnit; }

    public boolean isRequiresImmediateAttention() { return requiresImmediateAttention; }
    public void setRequiresImmediateAttention(boolean requiresImmediateAttention) { this.requiresImmediateAttention = requiresImmediateAttention; }
}
