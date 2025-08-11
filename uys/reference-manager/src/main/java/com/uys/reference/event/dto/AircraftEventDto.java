package com.uys.reference.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AircraftEventDto {
    
    private String eventId;
    private String eventType; // CREATED, UPDATED, DELETED, STATUS_CHANGED
    private String registration;
    private String aircraftType;
    private String model;
    private String manufacturer;
    private Integer capacity;
    private Integer maxRange;
    private Integer cruiseSpeed;
    private String airlineCode; // Reference to airline
    private String airlineName; // Denormalized for easier consumption
    private Boolean active;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime eventTimestamp;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    @Builder.Default
    private String sourceService = "reference-manager";
    @Builder.Default
    private String version = "1.0";
} 