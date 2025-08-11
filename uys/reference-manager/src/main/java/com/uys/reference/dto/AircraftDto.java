package com.uys.reference.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Aircraft DTO - Uçak veri transfer nesnesi
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
public class AircraftDto {

    /**
     * Aircraft Response DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String registration;
        private String aircraftType;
        private String model;
        private String manufacturer;
        private Integer capacity;
        private Integer maxRange;
        private Integer cruiseSpeed;
        private AirlineDto.Response airline;
        private Boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long version;
    }

    /**
     * Aircraft Create Request DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "Registration is required")
        @Size(min = 5, max = 10, message = "Registration must be between 5 and 10 characters")
        private String registration;

        @NotBlank(message = "Aircraft type is required")
        @Size(min = 2, max = 20, message = "Aircraft type must be between 2 and 20 characters")
        private String aircraftType;

        @Size(max = 100, message = "Model must not exceed 100 characters")
        private String model;

        @Size(max = 50, message = "Manufacturer must not exceed 50 characters")
        private String manufacturer;

        private Integer capacity;
        private Integer maxRange;
        private Integer cruiseSpeed;

        @NotNull(message = "Airline ID is required")
        private Long airlineId;

        @Builder.Default
        private Boolean active = true;
    }

    /**
     * Aircraft Update Request DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @NotBlank(message = "Aircraft type is required")
        @Size(min = 2, max = 20, message = "Aircraft type must be between 2 and 20 characters")
        private String aircraftType;

        @Size(max = 100, message = "Model must not exceed 100 characters")
        private String model;

        @Size(max = 50, message = "Manufacturer must not exceed 50 characters")
        private String manufacturer;

        private Integer capacity;
        private Integer maxRange;
        private Integer cruiseSpeed;
        private Boolean active;
    }

    /**
     * Aircraft List Response DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private Long id;
        private String registration;
        private String aircraftType;
        private String model;
        private String manufacturer;
        private Integer capacity;
        private String airlineCode;
        private String airlineName;
        private Boolean active;
    }
} 