package com.uys.flight.entity;

import com.uys.flight.enums.FlightStatus;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Flight Entity - Uçuş bilgileri
 */
@Entity
@Table(name = "flights", indexes = {
    @Index(name = "idx_flight_number", columnList = "flight_number", unique = true),
    @Index(name = "idx_airline_code", columnList = "airline_code"),
    @Index(name = "idx_aircraft_registration", columnList = "aircraft_registration"),
    @Index(name = "idx_departure_station", columnList = "departure_station_code"),
    @Index(name = "idx_arrival_station", columnList = "arrival_station_code"),
    @Index(name = "idx_scheduled_departure", columnList = "scheduled_departure_time"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_flight_date", columnList = "flight_date")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Flight number (e.g., TK123)
     */
    @NotBlank(message = "Flight number is required")
    @Size(min = 3, max = 10, message = "Flight number must be between 3 and 10 characters")
    @Column(name = "flight_number", nullable = false, unique = true, length = 10)
    private String flightNumber;

    /**
     * Airline code (IATA/ICAO)
     */
    @NotBlank(message = "Airline code is required")
    @Size(min = 2, max = 3, message = "Airline code must be between 2 and 3 characters")
    @Column(name = "airline_code", nullable = false, length = 3)
    private String airlineCode;

    /**
     * Aircraft registration
     */
    @NotBlank(message = "Aircraft registration is required")
    @Size(min = 5, max = 20, message = "Aircraft registration must be between 5 and 20 characters")
    @Column(name = "aircraft_registration", nullable = false, length = 20)
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
     * Flight date (YYYY-MM-DD)
     */
    @NotNull(message = "Flight date is required")
    @Column(name = "flight_date", nullable = false)
    private LocalDateTime flightDate;

    /**
     * Scheduled departure time
     */
    @NotNull(message = "Scheduled departure time is required")
    @Column(name = "scheduled_departure_time", nullable = false)
    private LocalDateTime scheduledDepartureTime;

    /**
     * Scheduled arrival time
     */
    @NotNull(message = "Scheduled arrival time is required")
    @Column(name = "scheduled_arrival_time", nullable = false)
    private LocalDateTime scheduledArrivalTime;

    /**
     * Actual departure time
     */
    @Column(name = "actual_departure_time")
    private LocalDateTime actualDepartureTime;

    /**
     * Actual arrival time
     */
    @Column(name = "actual_arrival_time")
    private LocalDateTime actualArrivalTime;

    /**
     * Flight status
     */
    @NotNull(message = "Flight status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private FlightStatus status;

    /**
     * Flight type
     */
    @NotNull(message = "Flight type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "flight_type", nullable = false, length = 20)
    private FlightType flightType;

    /**
     * Gate number
     */
    @Size(max = 10, message = "Gate number must not exceed 10 characters")
    @Column(name = "gate", length = 10)
    private String gate;

    /**
     * Terminal
     */
    @Size(max = 10, message = "Terminal must not exceed 10 characters")
    @Column(name = "terminal", length = 10)
    private String terminal;

    /**
     * Delay in minutes
     */
    @Column(name = "delay_minutes")
    private Integer delayMinutes;

    /**
     * Flight duration in minutes
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
     * Booked passengers
     */
    @Column(name = "booked_passengers")
    private Integer bookedPassengers;

    /**
     * Flight legs (for multi-leg flights)
     */
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<FlightLeg> flightLegs = new ArrayList<>();

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
     * Check if flight is delayed
     */
    public boolean isDelayed() {
        return delayMinutes != null && delayMinutes > 0;
    }

    /**
     * Check if flight is departed
     */
    public boolean isDeparted() {
        return actualDepartureTime != null;
    }

    /**
     * Check if flight is arrived
     */
    public boolean isArrived() {
        return actualArrivalTime != null;
    }

    /**
     * Calculate load factor (passenger percentage)
     */
    public Double getLoadFactor() {
        if (passengerCapacity == null || passengerCapacity == 0 || bookedPassengers == null) {
            return null;
        }
        return (bookedPassengers.doubleValue() / passengerCapacity.doubleValue()) * 100;
    }

    /**
     * Mark flight as departed
     */
    public void markDeparted(LocalDateTime departureTime) {
        this.actualDepartureTime = departureTime;
        this.status = FlightStatus.DEPARTED;
    }

    /**
     * Mark flight as arrived
     */
    public void markArrived(LocalDateTime arrivalTime) {
        this.actualArrivalTime = arrivalTime;
        this.status = FlightStatus.ARRIVED;
    }

    /**
     * Cancel flight
     */
    public void cancel() {
        this.status = FlightStatus.CANCELLED;
    }

    /**
     * Delay flight
     */
    public void delay(int minutes) {
        this.delayMinutes = minutes;
        this.status = FlightStatus.DELAYED;
        this.scheduledDepartureTime = this.scheduledDepartureTime.plusMinutes(minutes);
        this.scheduledArrivalTime = this.scheduledArrivalTime.plusMinutes(minutes);
    }
}