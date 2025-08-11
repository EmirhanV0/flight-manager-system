package com.uys.reference.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Airline DTO - Havayolu şirketi veri transfer nesnesi
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
public class AirlineDto {

    /**
     * Airline Response DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Airline response model")
    public static class Response {
        
        @Schema(description = "Airline unique identifier", example = "1")
        private Long id;
        
        @Schema(description = "Airline IATA/ICAO code", example = "TK")
        private String airlineCode;
        
        @Schema(description = "Airline full name", example = "Turkish Airlines")
        private String airlineName;
        
        @Schema(description = "Airline description", example = "National flag carrier of Turkey")
        private String description;
        
        @Schema(description = "Country of airline", example = "Turkey")
        private String country;
        
        @Schema(description = "Primary city/hub", example = "Istanbul")
        private String city;
        
        @Schema(description = "Active status", example = "true")
        private Boolean active;
        
        @Schema(description = "Creation timestamp")
        private LocalDateTime createdAt;
        
        @Schema(description = "Last update timestamp")
        private LocalDateTime updatedAt;
        
        @Schema(description = "Entity version for optimistic locking", example = "0")
        private Long version;
    }

    /**
     * Airline Create Request DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "Airline code is required")
        @Size(min = 2, max = 3, message = "Airline code must be between 2 and 3 characters")
        private String airlineCode;

        @NotBlank(message = "Airline name is required")
        @Size(min = 2, max = 100, message = "Airline name must be between 2 and 100 characters")
        private String airlineName;

        @Size(max = 200, message = "Description must not exceed 200 characters")
        private String description;

        @Size(max = 50, message = "Country must not exceed 50 characters")
        private String country;

        @Size(max = 50, message = "City must not exceed 50 characters")
        private String city;

        @Builder.Default
        private Boolean active = true;
    }

    /**
     * Airline Update Request DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @NotBlank(message = "Airline name is required")
        @Size(min = 2, max = 100, message = "Airline name must be between 2 and 100 characters")
        private String airlineName;

        @Size(max = 200, message = "Description must not exceed 200 characters")
        private String description;

        @Size(max = 50, message = "Country must not exceed 50 characters")
        private String country;

        @Size(max = 50, message = "City must not exceed 50 characters")
        private String city;

        private Boolean active;
    }

    /**
     * Airline List Response DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private Long id;
        private String airlineCode;
        private String airlineName;
        private String country;
        private String city;
        private Boolean active;
    }
} 