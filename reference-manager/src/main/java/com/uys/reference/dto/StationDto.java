package com.uys.reference.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Station DTO - Havaalanı veri transfer nesnesi
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
public class StationDto {

    /**
     * Station Response DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String stationCode;
        private String stationName;
        private String city;
        private String country;
        private String address;
        private String timezone;
        private Double latitude;
        private Double longitude;
        private Integer altitude;
        private String description;
        private Boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long version;
    }

    /**
     * Station Create Request DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "Station code is required")
        @Size(min = 3, max = 3, message = "Station code must be exactly 3 characters")
        private String stationCode;

        @NotBlank(message = "Station name is required")
        @Size(min = 2, max = 100, message = "Station name must be between 2 and 100 characters")
        private String stationName;

        @Size(max = 50, message = "City must not exceed 50 characters")
        private String city;

        @Size(max = 50, message = "Country must not exceed 50 characters")
        private String country;

        @Size(max = 100, message = "Address must not exceed 100 characters")
        private String address;

        @Size(max = 20, message = "Timezone must not exceed 20 characters")
        private String timezone;

        private Double latitude;
        private Double longitude;
        private Integer altitude;

        @Size(max = 200, message = "Description must not exceed 200 characters")
        private String description;

        @Builder.Default
        private Boolean active = true;
    }

    /**
     * Station Update Request DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @NotBlank(message = "Station name is required")
        @Size(min = 2, max = 100, message = "Station name must be between 2 and 100 characters")
        private String stationName;

        @Size(max = 50, message = "City must not exceed 50 characters")
        private String city;

        @Size(max = 50, message = "Country must not exceed 50 characters")
        private String country;

        @Size(max = 100, message = "Address must not exceed 100 characters")
        private String address;

        @Size(max = 20, message = "Timezone must not exceed 20 characters")
        private String timezone;

        private Double latitude;
        private Double longitude;
        private Integer altitude;

        @Size(max = 200, message = "Description must not exceed 200 characters")
        private String description;

        private Boolean active;
    }

    /**
     * Station List Response DTO
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private Long id;
        private String stationCode;
        private String stationName;
        private String city;
        private String country;
        private String timezone;
        private Boolean active;
    }
} 