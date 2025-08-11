package com.uys.archive.repository;

import com.uys.archive.entity.EventLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Event Log Repository
 */
@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long> {

    Page<EventLog> findByEventType(String eventType, Pageable pageable);

    Page<EventLog> findBySourceService(String sourceService, Pageable pageable);

    Page<EventLog> findByEntityType(String entityType, Pageable pageable);

    List<EventLog> findByEntityIdAndEntityType(String entityId, String entityType);

    Page<EventLog> findByEventTimestampBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    @Query("SELECT e FROM EventLog e WHERE e.correlationId = :correlationId ORDER BY e.eventTimestamp")
    List<EventLog> findByCorrelationIdOrderByEventTimestamp(@Param("correlationId") String correlationId);

    @Query("SELECT e FROM EventLog e WHERE e.eventTimestamp < :cutoffDate")
    List<EventLog> findOldEvents(@Param("cutoffDate") LocalDateTime cutoffDate);

    long countByEventType(String eventType);

    long countBySourceService(String sourceService);
}