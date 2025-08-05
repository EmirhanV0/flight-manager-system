package com.uys.reference.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Error Response DTO for API documentation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API Error Response")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error code", example = "VALIDATION_ERROR")
    private String error;

    @Schema(description = "Human readable error message", example = "Validation failed for request")
    private String message;

    @Schema(description = "Request path where error occurred", example = "/api/v1/airlines")
    private String path;

    @Schema(description = "Timestamp when error occurred")
    private LocalDateTime timestamp;

    @Schema(description = "Detailed validation errors")
    private List<ValidationError> details;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Validation Error Detail")
    public static class ValidationError {
        
        @Schema(description = "Field name that failed validation", example = "airlineCode")
        private String field;
        
        @Schema(description = "Rejected value", example = "")
        private Object rejectedValue;
        
        @Schema(description = "Validation error message", example = "Airline code is required")
        private String message;
    }
}