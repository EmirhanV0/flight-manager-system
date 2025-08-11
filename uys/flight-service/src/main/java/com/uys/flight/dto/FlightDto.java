package com.uys.flight.dto;

import com.uys.flight.enums.FlightStatus;
import com.uys.flight.enums.FlightType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Flight DTOs
 */
public class FlightDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Flight response model")
    public static class Response {
        @Schema(description = "Flight ID", example = "1")
        private Long id;
        
        @Schema(description = "Flight number", example = "TK123")
        private String flightNumber;
        
        @Schema(description = "Airline code", example = "TK")
        private String airlineCode;
        
        @Schema(description = "Aircraft registration", example = "TC-JJA")
        private String aircraftRegistration;
        
        @Schema(description = "Departure station", example = "IST")
        private String departureStationCode;
        
        @Schema(description = "Arrival station", example = "JFK")
        private String arrivalStationCode;
        
        @Schema(description = "Flight date")
        private LocalDateTime flightDate;
        
        @Schema(description = "Scheduled departure time")
        private LocalDateTime scheduledDepartureTime;
        
        @Schema(description = "Scheduled arrival time")
        private LocalDateTime scheduledArrivalTime;
        
        @Schema(description = "Actual departure time")
        private LocalDateTime actualDepartureTime;
        
        @Schema(description = "Actual arrival time")
        private LocalDateTime actualArrivalTime;
        
        @Schema(description = "Flight status")
        private FlightStatus status;
        
        @Schema(description = "Flight type")
        private FlightType flightType;
        
        @Schema(description = "Gate", example = "A12")
        private String gate;
        
        @Schema(description = "Terminal", example = "1")
        private String terminal;
        
        @Schema(description = "Delay in minutes")
        private Integer delayMinutes;
        
        @Schema(description = "Duration in minutes")
        private Integer durationMinutes;
        
        @Schema(description = "Distance in km")
        private Integer distanceKm;
        
        @Schema(description = "Passenger capacity")
        private Integer passengerCapacity;
        
        @Schema(description = "Booked passengers")
        private Integer bookedPassengers;
        
        @Schema(description = "Load factor percentage")
        private Double loadFactor;
        
        @Schema(description = "Flight legs")
        private List<FlightLegDto.Response> flightLegs;
        
        @Schema(description = "Description")
        private String description;
        
        @Schema(description = "Active status")
        private Boolean active;
        
        @Schema(description = "Version")
        private Long version;
        
        @Schema(description = "Created at")
        private LocalDateTime createdAt;
        
        @Schema(description = "Updated at")
        private LocalDateTime updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Flight creation request")
    public static class CreateRequest {
        @Schema(description = "Flight number", example = "TK123", required = true)
        @NotBlank(message = "Flight number is required")
        @Size(min = 3, max = 10)
        private String flightNumber;
        
        @Schema(description = "Airline code", example = "TK", required = true)
        @NotBlank(message = "Airline code is required")
        @Size(min = 2, max = 3)
        private String airlineCode;
        
        @Schema(description = "Aircraft registration", example = "TC-JJA", required = true)
        @NotBlank(message = "Aircraft registration is required")
        @Size(min = 5, max = 20)
        private String aircraftRegistration;
        
        @Schema(description = "Departure station", example = "IST", required = true)
        @NotBlank(message = "Departure station is required")
        @Size(min = 3, max = 4)
        private String departureStationCode;
        
        @Schema(description = "Arrival station", example = "JFK", required = true)
        @NotBlank(message = "Arrival station is required")
        @Size(min = 3, max = 4)
        private String arrivalStationCode;
        
        @Schema(description = "Flight date", required = true)
        @NotNull(message = "Flight date is required")
        private LocalDateTime flightDate;
        
        @Schema(description = "Scheduled departure time", required = true)
        @NotNull(message = "Scheduled departure time is required")
        private LocalDateTime scheduledDepartureTime;
        
        @Schema(description = "Scheduled arrival time", required = true)
        @NotNull(message = "Scheduled arrival time is required")
        private LocalDateTime scheduledArrivalTime;
        
        @Schema(description = "Flight type", required = true)
        @NotNull(message = "Flight type is required")
        private FlightType flightType;
        
        @Schema(description = "Gate", example = "A12")
        private String gate;
        
        @Schema(description = "Terminal", example = "1")
        private String terminal;
        
        @Schema(description = "Duration in minutes")
        private Integer durationMinutes;
        
        @Schema(description = "Distance in km")
        private Integer distanceKm;
        
        @Schema(description = "Passenger capacity")
        private Integer passengerCapacity;
        
        @Schema(description = "Description")
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Flight update request")
    public static class UpdateRequest {
        @Schema(description = "Gate", example = "A12")
        private String gate;
        
        @Schema(description = "Terminal", example = "1")
        private String terminal;
        
        @Schema(description = "Actual departure time")
        private LocalDateTime actualDepartureTime;
        
        @Schema(description = "Actual arrival time")
        private LocalDateTime actualArrivalTime;
        
        @Schema(description = "Flight status")
        private FlightStatus status;
        
        @Schema(description = "Booked passengers")
        private Integer bookedPassengers;
        
        @Schema(description = "Description")
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Flight list response")
    public static class ListResponse {
        private Long id;
        private String flightNumber;
        private String airlineCode;
        private String departureStationCode;
        private String arrivalStationCode;
        private LocalDateTime scheduledDepartureTime;
        private LocalDateTime scheduledArrivalTime;
        private FlightStatus status;
        private FlightType flightType;
        private Integer delayMinutes;
        private Boolean active;
    }
}