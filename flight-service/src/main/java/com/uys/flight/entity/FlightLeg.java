package com.uys.flight.entity;

import com.uys.flight.enums.FlightStatus;
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

/**
 * Flight Leg Entity - Uçuş bacağı (Multi-leg flights için)
 */
@Entity
@Table(name = "flight_legs", indexes = {
    @Index(name = "idx_flight_id", columnList = "flight_id"),
    @Index(name = "idx_leg_number", columnList = "leg_number"),
    @Index(name = "idx_departure_station_leg", columnList = "departure_station_code"),
    @Index(name = "idx_arrival_station_leg", columnList = "arrival_station_code"),
    @Index(name = "idx_scheduled_departure_leg", columnList = "scheduled_departure_time")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FlightLeg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Parent flight
     */
    @NotNull(message = "Flight is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    /**
     * Leg number (sequence in the flight)
     */
    @NotNull(message = "Leg number is required")
    @Column(name = "leg_number", nullable = false)
    private Integer legNumber;

    /**
     * Departure station code for this leg
     */
    @NotBlank(message = "Departure station is required")
    @Size(min = 3, max = 4, message = "Station code must be between 3 and 4 characters")
    @Column(name = "departure_station_code", nullable = false, length = 4)
    private String departureStationCode;

    /**
     * Arrival station code for this leg
     */
    @NotBlank(message = "Arrival station is required")
    @Size(min = 3, max = 4, message = "Station code must be between 3 and 4 characters")
    @Column(name = "arrival_station_code", nullable = false, length = 4)
    private String arrivalStationCode;

    /**
     * Scheduled departure time for this leg
     */
    @NotNull(message = "Scheduled departure time is required")
    @Column(name = "scheduled_departure_time", nullable = false)
    private LocalDateTime scheduledDepartureTime;

    /**
     * Scheduled arrival time for this leg
     */
    @NotNull(message = "Scheduled arrival time is required")
    @Column(name = "scheduled_arrival_time", nullable = false)
    private LocalDateTime scheduledArrivalTime;

    /**
     * Actual departure time for this leg
     */
    @Column(name = "actual_departure_time")
    private LocalDateTime actualDepartureTime;

    /**
     * Actual arrival time for this leg
     */
    @Column(name = "actual_arrival_time")
    private LocalDateTime actualArrivalTime;

    /**
     * Status of this leg
     */
    @NotNull(message = "Flight status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private FlightStatus status;

    /**
     * Gate number for this leg
     */
    @Size(max = 10, message = "Gate number must not exceed 10 characters")
    @Column(name = "gate", length = 10)
    private String gate;

    /**
     * Terminal for this leg
     */
    @Size(max = 10, message = "Terminal must not exceed 10 characters")
    @Column(name = "terminal", length = 10)
    private String terminal;

    /**
     * Delay in minutes for this leg
     */
    @Column(name = "delay_minutes")
    private Integer delayMinutes;

    /**
     * Duration of this leg in minutes
     */
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    /**
     * Distance of this leg in kilometers
     */
    @Column(name = "distance_km")
    private Integer distanceKm;

    /**
     * Ground time before next leg (for connecting flights)
     */
    @Column(name = "ground_time_minutes")
    private Integer groundTimeMinutes;

    /**
     * Description/Notes for this leg
     */
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description", length = 500)
    private String description;

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
     * Check if leg is delayed
     */
    public boolean isDelayed() {
        return delayMinutes != null && delayMinutes > 0;
    }

    /**
     * Check if leg is departed
     */
    public boolean isDeparted() {
        return actualDepartureTime != null;
    }

    /**
     * Check if leg is arrived
     */
    public boolean isArrived() {
        return actualArrivalTime != null;
    }

    /**
     * Mark leg as departed
     */
    public void markDeparted(LocalDateTime departureTime) {
        this.actualDepartureTime = departureTime;
        this.status = FlightStatus.DEPARTED;
    }

    /**
     * Mark leg as arrived
     */
    public void markArrived(LocalDateTime arrivalTime) {
        this.actualArrivalTime = arrivalTime;
        this.status = FlightStatus.ARRIVED;
    }

    /**
     * Delay leg
     */
    public void delay(int minutes) {
        this.delayMinutes = minutes;
        this.status = FlightStatus.DELAYED;
        this.scheduledDepartureTime = this.scheduledDepartureTime.plusMinutes(minutes);
        this.scheduledArrivalTime = this.scheduledArrivalTime.plusMinutes(minutes);
    }
}