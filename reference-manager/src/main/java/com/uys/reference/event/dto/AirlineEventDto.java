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
public class AirlineEventDto {
    
    private String eventId;
    private String eventType; // CREATED, UPDATED, DELETED, STATUS_CHANGED
    private String airlineCode;
    private String airlineName;
    private String country;
    private String city;
    private String description;
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