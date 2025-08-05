package com.uys.flight.entity;

import com.uys.flight.enums.FlightType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

/**
 * Flight Schedule Entity - Uçuş programı (recurring flights)
 */
@Entity
@Table(name = "flight_schedules", indexes = {
    @Index(name = "idx_schedule_flight_number", columnList = "flight_number"),
    @Index(name = "idx_schedule_airline_code", columnList = "airline_code"),
    @Index(name = "idx_schedule_departure_station", columnList = "departure_station_code"),
    @Index(name = "idx_schedule_arrival_station", columnList = "arrival_station_code"),
    @Index(name = "idx_schedule_effective_from", columnList = "effective_from"),
    @Index(name = "idx_schedule_effective_to", columnList = "effective_to"),
    @Index(name = "idx_schedule_active", columnList = "active")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FlightSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Flight number pattern (e.g., TK123)
     */
    @NotBlank(message = "Flight number is required")
    @Size(min = 3, max = 10, message = "Flight number must be between 3 and 10 characters")
    @Column(name = "flight_number", nullable = false, length = 10)
    private String flightNumber;

    /**
     * Airline code (IATA/ICAO)
     */
    @NotBlank(message = "Airline code is required")
    @Size(min = 2, max = 3, message = "Airline code must be between 2 and 3 characters")
    @Column(name = "airline_code", nullable = false, length = 3)
    private String airlineCode;

    /**
     * Aircraft registration (default aircraft for this schedule)
     */
    @Size(min = 5, max = 20, message = "Aircraft registration must be between 5 and 20 characters")
    @Column(name = "aircraft_registration", length = 20)
    private String aircraftRegistration;

    /**
     * Departure station code
     */
    @NotBlank(message = "Departure station is required")
    @Size(min = 3, max = 4, message = "Station code must be between 3 and 4 characters")
    @Column(name = "departure_station_code", nullable = false, length = 4)
    private String departureStationCode;

    /**
     * Arrival station code
     */
    @NotBlank(message = "Arrival station is required")
    @Size(min = 3, max = 4, message = "Station code must be between 3 and 4 characters")
    @Column(name = "arrival_station_code", nullable = false, length = 4)
    private String arrivalStationCode;

    /**
     * Scheduled departure time (time only)
     */
    @NotNull(message = "Departure time is required")
    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    /**
     * Scheduled arrival time (time only)
     */
    @NotNull(message = "Arrival time is required")
    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;

    /**
     * Flight type
     */
    @NotNull(message = "Flight type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "flight_type", nullable = false, length = 20)
    private FlightType flightType;

    /**
     * Days of week this schedule operates (stored as comma-separated string)
     * Example: "MONDAY,WEDNESDAY,FRIDAY"
     */
    @Column(name = "operating_days", length = 100)
    private String operatingDays;

    /**
     * Effective from date
     */
    @NotNull(message = "Effective from date is required")
    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    /**
     * Effective to date
     */
    @NotNull(message = "Effective to date is required")
    @Column(name = "effective_to", nullable = false)
    private LocalDate effectiveTo;

    /**
     * Duration in minutes
     */
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    /**
     * Distance in kilometers
     */
    @Column(name = "distance_km")
    private Integer distanceKm;

    /**
     * Passenger capacity
     */
    @Column(name = "passenger_capacity")
    private Integer passengerCapacity;

    /**
     * Terminal
     */
    @Size(max = 10, message = "Terminal must not exceed 10 characters")
    @Column(name = "terminal", length = 10)
    private String terminal;

    /**
     * Description/Notes
     */
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * Active flag
     */
    @Builder.Default
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    /**
     * Version for optimistic locking
     */
    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    /**
     * Creation timestamp
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Last update timestamp
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Helper methods

    /**
     * Check if schedule is valid for given date
     */
    public boolean isValidForDate(LocalDate date) {
        return !date.isBefore(effectiveFrom) && !date.isAfter(effectiveTo);
    }

    /**
     * Check if schedule operates on given day of week
     */
    public boolean operatesOnDay(DayOfWeek dayOfWeek) {
        if (operatingDays == null || operatingDays.isEmpty()) {
            return true; // Operates daily if no specific days defined
        }
        return operatingDays.contains(dayOfWeek.name());
    }

    /**
     * Set operating days from Set of DayOfWeek
     */
    public void setOperatingDays(Set<DayOfWeek> days) {
        if (days == null || days.isEmpty()) {
            this.operatingDays = null;
        } else {
            this.operatingDays = String.join(",", 
                days.stream().map(DayOfWeek::name).toArray(String[]::new));
        }
    }

    /**
     * Get operating days as Set of DayOfWeek
     */
    public Set<DayOfWeek> getOperatingDaysAsSet() {
        if (operatingDays == null || operatingDays.isEmpty()) {
            return Set.of(DayOfWeek.values()); // All days if not specified
        }
        
        String[] days = operatingDays.split(",");
        return Set.of(days).stream()
            .map(DayOfWeek::valueOf)
            .collect(java.util.stream.Collectors.toSet());
    }

    /**
     * Create a Flight instance from this schedule for given date
     */
    public Flight createFlightForDate(LocalDate date) {
        if (!isValidForDate(date) || !operatesOnDay(date.getDayOfWeek())) {
            throw new IllegalArgumentException("Schedule does not operate on date: " + date);
        }

        LocalDateTime departureDateTime = date.atTime(departureTime);
        LocalDateTime arrivalDateTime = date.atTime(arrivalTime);
        
        // Handle overnight flights
        if (arrivalTime.isBefore(departureTime)) {
            arrivalDateTime = arrivalDateTime.plusDays(1);
        }

        return Flight.builder()
            .flightNumber(flightNumber)
            .airlineCode(airlineCode)
            .aircraftRegistration(aircraftRegistration)
            .departureStationCode(departureStationCode)
            .arrivalStationCode(arrivalStationCode)
            .flightDate(departureDateTime)
            .scheduledDepartureTime(departureDateTime)
            .scheduledArrivalTime(arrivalDateTime)
            .flightType(flightType)
            .durationMinutes(durationMinutes)
            .distanceKm(distanceKm)
            .passengerCapacity(passengerCapacity)
            .terminal(terminal)
            .status(com.uys.flight.enums.FlightStatus.SCHEDULED)
            .active(true)
            .build();
    }

    /**
     * Deactivate schedule
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Activate schedule
     */
    public void activate() {
        this.active = true;
    }
}