package com.uys.reference.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
 * Airline Entity - Havayolu şirketi bilgilerini tutar
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@Entity
@Table(name = "airline", indexes = {
    @Index(name = "idx_airline_code", columnList = "airline_code", unique = true),
    @Index(name = "idx_airline_name", columnList = "airline_name"),
    @Index(name = "idx_airline_active", columnList = "active")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Airline code is required")
    @Size(min = 2, max = 3, message = "Airline code must be between 2 and 3 characters")
    @Column(name = "airline_code", length = 3, nullable = false, unique = true)
    private String airlineCode;

    @NotBlank(message = "Airline name is required")
    @Size(min = 2, max = 100, message = "Airline name must be between 2 and 100 characters")
    @Column(name = "airline_name", length = 100, nullable = false)
    private String airlineName;

    @Size(max = 200, message = "Description must not exceed 200 characters")
    @Column(name = "description", length = 200)
    private String description;

    @Size(max = 50, message = "Country must not exceed 50 characters")
    @Column(name = "country", length = 50)
    private String country;

    @Size(max = 50, message = "City must not exceed 50 characters")
    @Column(name = "city", length = 50)
    private String city;

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
     * Airline'ın aktif olup olmadığını kontrol eder
     * 
     * @return true if airline is active, false otherwise
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }

    /**
     * Airline'ı aktif hale getirir
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Airline'ı pasif hale getirir
     */
    public void deactivate() {
        this.active = false;
    }
} 