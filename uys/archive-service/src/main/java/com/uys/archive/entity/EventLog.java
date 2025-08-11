package com.uys.archive.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Event Log Entity - Sistem event'lerinin arşivi
 */
@Entity
@Table(name = "event_logs", indexes = {
    @Index(name = "idx_event_type", columnList = "event_type"),
    @Index(name = "idx_source_service", columnList = "source_service"),
    @Index(name = "idx_event_timestamp", columnList = "event_timestamp"),
    @Index(name = "idx_entity_type", columnList = "entity_type"),
    @Index(name = "idx_entity_id", columnList = "entity_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event type is required")
    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @NotBlank(message = "Source service is required")
    @Column(name = "source_service", nullable = false, length = 50)
    private String sourceService;

    @NotBlank(message = "Entity type is required")
    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType;

    @NotBlank(message = "Entity ID is required")
    @Column(name = "entity_id", nullable = false, length = 100)
    private String entityId;

    @NotNull(message = "Event timestamp is required")
    @Column(name = "event_timestamp", nullable = false)
    private LocalDateTime eventTimestamp;

    @Column(name = "event_data", columnDefinition = "TEXT")
    private String eventData;

    @Column(name = "correlation_id", length = 100)
    private String correlationId;

    @Column(name = "user_id", length = 100)
    private String userId;

    @CreatedDate
    @Column(name = "archived_at", nullable = false, updatable = false)
    private LocalDateTime archivedAt;
}