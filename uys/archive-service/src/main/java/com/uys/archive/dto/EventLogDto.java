package com.uys.archive.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event Log DTOs
 */
public class EventLogDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Event log response")
    public static class Response {
        @Schema(description = "Event log ID")
        private Long id;
        
        @Schema(description = "Event type")
        private String eventType;
        
        @Schema(description = "Source service")
        private String sourceService;
        
        @Schema(description = "Entity type")
        private String entityType;
        
        @Schema(description = "Entity ID")
        private String entityId;
        
        @Schema(description = "Event timestamp")
        private LocalDateTime eventTimestamp;
        
        @Schema(description = "Event data")
        private String eventData;
        
        @Schema(description = "Correlation ID")
        private String correlationId;
        
        @Schema(description = "User ID")
        private String userId;
        
        @Schema(description = "Archived timestamp")
        private LocalDateTime archivedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Event log creation request")
    public static class CreateRequest {
        @Schema(description = "Event type", required = true)
        @NotBlank(message = "Event type is required")
        private String eventType;
        
        @Schema(description = "Source service", required = true)
        @NotBlank(message = "Source service is required")
        private String sourceService;
        
        @Schema(description = "Entity type", required = true)
        @NotBlank(message = "Entity type is required")
        private String entityType;
        
        @Schema(description = "Entity ID", required = true)
        @NotBlank(message = "Entity ID is required")
        private String entityId;
        
        @Schema(description = "Event timestamp", required = true)
        @NotNull(message = "Event timestamp is required")
        private LocalDateTime eventTimestamp;
        
        @Schema(description = "Event data (JSON)")
        private String eventData;
        
        @Schema(description = "Correlation ID")
        private String correlationId;
        
        @Schema(description = "User ID")
        private String userId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Event log list response")
    public static class ListResponse {
        private Long id;
        private String eventType;
        private String sourceService;
        private String entityType;
        private String entityId;
        private LocalDateTime eventTimestamp;
        private String correlationId;
        private LocalDateTime archivedAt;
    }
}