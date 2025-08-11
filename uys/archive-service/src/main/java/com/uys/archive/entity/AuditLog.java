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
 * Audit Log Entity - Sistem audit kayıtları
 */
@Entity
@Table(name = "audit_logs", indexes = {
    @Index(name = "idx_audit_action", columnList = "action"),
    @Index(name = "idx_audit_service", columnList = "service_name"),
    @Index(name = "idx_audit_timestamp", columnList = "audit_timestamp"),
    @Index(name = "idx_audit_user", columnList = "user_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Action is required")
    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @NotBlank(message = "Service name is required")
    @Column(name = "service_name", nullable = false, length = 50)
    private String serviceName;

    @Column(name = "resource_type", length = 50)
    private String resourceType;

    @Column(name = "resource_id", length = 100)
    private String resourceId;

    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @NotNull(message = "Audit timestamp is required")
    @Column(name = "audit_timestamp", nullable = false)
    private LocalDateTime auditTimestamp;

    @Column(name = "old_values", columnDefinition = "TEXT")
    private String oldValues;

    @Column(name = "new_values", columnDefinition = "TEXT")
    private String newValues;

    @Column(name = "description", length = 1000)
    private String description;

    @Builder.Default
    @Column(name = "success", nullable = false)
    private Boolean success = true;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}