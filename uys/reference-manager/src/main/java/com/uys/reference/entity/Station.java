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

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Station Entity - Havaalanı bilgilerini tutar
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@Entity
@Table(name = "station", indexes = {
    @Index(name = "idx_station_code", columnList = "station_code", unique = true),
    @Index(name = "idx_station_name", columnList = "station_name"),
    @Index(name = "idx_station_city", columnList = "city"),
    @Index(name = "idx_station_country", columnList = "country"),
    @Index(name = "idx_station_active", columnList = "active")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Station code is required")
    @Size(min = 3, max = 3, message = "Station code must be exactly 3 characters")
    @Column(name = "station_code", length = 3, nullable = false, unique = true)
    private String stationCode;

    @NotBlank(message = "Station name is required")
    @Size(min = 2, max = 100, message = "Station name must be between 2 and 100 characters")
    @Column(name = "station_name", length = 100, nullable = false)
    private String stationName;

    @Size(max = 50, message = "City must not exceed 50 characters")
    @Column(name = "city", length = 50)
    private String city;

    @Size(max = 50, message = "Country must not exceed 50 characters")
    @Column(name = "country", length = 50)
    private String country;

    @Size(max = 100, message = "Address must not exceed 100 characters")
    @Column(name = "address", length = 100)
    private String address;

    @Size(max = 20, message = "Timezone must not exceed 20 characters")
    @Column(name = "timezone", length = 20)
    private String timezone;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "altitude")
    private Integer altitude;

    @Size(max = 200, message = "Description must not exceed 200 characters")
    @Column(name = "description", length = 200)
    private String description;

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
     * Station'ın aktif olup olmadığını kontrol eder
     * 
     * @return true if station is active, false otherwise
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }

    /**
     * Station'ı aktif hale getirir
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Station'ı pasif hale getirir
     */
    public void deactivate() {
        this.active = false;
    }
} 