package com.uys.reference.entity;

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
 * Aircraft Entity - Uçak bilgilerini tutar
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@Entity
@Table(name = "aircraft", indexes = {
    @Index(name = "idx_aircraft_registration", columnList = "registration", unique = true),
    @Index(name = "idx_aircraft_type", columnList = "aircraft_type"),
    @Index(name = "idx_airline_id", columnList = "airline_id"),
    @Index(name = "idx_aircraft_active", columnList = "active")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Registration is required")
    @Size(min = 5, max = 10, message = "Registration must be between 5 and 10 characters")
    @Column(name = "registration", length = 10, nullable = false, unique = true)
    private String registration;

    @NotBlank(message = "Aircraft type is required")
    @Size(min = 2, max = 20, message = "Aircraft type must be between 2 and 20 characters")
    @Column(name = "aircraft_type", length = 20, nullable = false)
    private String aircraftType;

    @Size(max = 100, message = "Model must not exceed 100 characters")
    @Column(name = "model", length = 100)
    private String model;

    @Size(max = 50, message = "Manufacturer must not exceed 50 characters")
    @Column(name = "manufacturer", length = 50)
    private String manufacturer;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "max_range")
    private Integer maxRange;

    @Column(name = "cruise_speed")
    private Integer cruiseSpeed;

    @NotNull(message = "Airline is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    /**
     * Aircraft'ın aktif olup olmadığını kontrol eder
     * 
     * @return true if aircraft is active, false otherwise
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }

    /**
     * Aircraft'ı aktif hale getirir
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Aircraft'ı pasif hale getirir
     */
    public void deactivate() {
        this.active = false;
    }
} 