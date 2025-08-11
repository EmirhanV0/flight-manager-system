package com.uys.flight.dto;

import com.uys.flight.enums.FlightStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * FlightLeg DTOs
 */
public class FlightLegDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Flight leg response")
    public static class Response {
        private Long id;
        private Integer legNumber;
        private String departureStationCode;
        private String arrivalStationCode;
        private LocalDateTime scheduledDepartureTime;
        private LocalDateTime scheduledArrivalTime;
        private LocalDateTime actualDepartureTime;
        private LocalDateTime actualArrivalTime;
        private FlightStatus status;
        private String gate;
        private String terminal;
        private Integer delayMinutes;
        private Integer durationMinutes;
        private Integer distanceKm;
    }
}