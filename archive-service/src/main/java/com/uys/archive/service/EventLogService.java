package com.uys.archive.service;

import com.uys.archive.dto.EventLogDto;
import com.uys.archive.entity.EventLog;
import com.uys.archive.mapper.EventLogMapper;
import com.uys.archive.repository.EventLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Event Log Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventLogService {

    private final EventLogRepository eventLogRepository;
    private final EventLogMapper eventLogMapper;

    public EventLogDto.Response createEventLog(EventLogDto.CreateRequest createRequest) {
        log.info("Creating event log: {} - {}", createRequest.getEventType(), createRequest.getEntityId());
        
        EventLog eventLog = eventLogMapper.toEntity(createRequest);
        EventLog savedEventLog = eventLogRepository.save(eventLog);
        
        log.info("Event log created: {}", savedEventLog.getId());
        return eventLogMapper.toResponse(savedEventLog);
    }

    @Transactional(readOnly = true)
    public EventLogDto.Response getEventLogById(Long id) {
        log.debug("Getting event log by ID: {}", id);
        
        EventLog eventLog = eventLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event log not found: " + id));
        
        return eventLogMapper.toResponse(eventLog);
    }

    @Transactional(readOnly = true)
    public Page<EventLogDto.ListResponse> getAllEventLogs(Pageable pageable) {
        log.debug("Getting all event logs with pagination: {}", pageable);
        
        Page<EventLog> eventLogs = eventLogRepository.findAll(pageable);
        return eventLogs.map(eventLogMapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public Page<EventLogDto.ListResponse> getEventLogsByType(String eventType, Pageable pageable) {
        log.debug("Getting event logs by type: {}", eventType);
        
        Page<EventLog> eventLogs = eventLogRepository.findByEventType(eventType, pageable);
        return eventLogs.map(eventLogMapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public Page<EventLogDto.ListResponse> getEventLogsByService(String sourceService, Pageable pageable) {
        log.debug("Getting event logs by service: {}", sourceService);
        
        Page<EventLog> eventLogs = eventLogRepository.findBySourceService(sourceService, pageable);
        return eventLogs.map(eventLogMapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public List<EventLogDto.Response> getEventLogsByEntity(String entityId, String entityType) {
        log.debug("Getting event logs by entity: {} - {}", entityType, entityId);
        
        List<EventLog> eventLogs = eventLogRepository.findByEntityIdAndEntityType(entityId, entityType);
        return eventLogMapper.toResponseList(eventLogs);
    }

    @Transactional(readOnly = true)
    public Page<EventLogDto.ListResponse> getEventLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        log.debug("Getting event logs by time range: {} to {}", startTime, endTime);
        
        Page<EventLog> eventLogs = eventLogRepository.findByEventTimestampBetween(startTime, endTime, pageable);
        return eventLogs.map(eventLogMapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public List<EventLogDto.Response> getEventLogsByCorrelation(String correlationId) {
        log.debug("Getting event logs by correlation ID: {}", correlationId);
        
        List<EventLog> eventLogs = eventLogRepository.findByCorrelationIdOrderByEventTimestamp(correlationId);
        return eventLogMapper.toResponseList(eventLogs);
    }

    public void deleteOldEventLogs(int retentionDays) {
        log.info("Deleting old event logs older than {} days", retentionDays);
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
        List<EventLog> oldEvents = eventLogRepository.findOldEvents(cutoffDate);
        
        if (!oldEvents.isEmpty()) {
            eventLogRepository.deleteAll(oldEvents);
            log.info("Deleted {} old event logs", oldEvents.size());
        }
    }
}