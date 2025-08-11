package com.uys.archive.controller;

import com.uys.archive.dto.EventLogDto;
import com.uys.archive.service.EventLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Event Log Controller
 */
@RestController
@RequestMapping("/event-logs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Event Log", description = "Event log arşivleme API'leri")
public class EventLogController {

    private final EventLogService eventLogService;

    @PostMapping
    @Operation(summary = "Yeni event log oluştur")
    public ResponseEntity<EventLogDto.Response> createEventLog(@Valid @RequestBody EventLogDto.CreateRequest createRequest) {
        log.info("Creating event log: {}", createRequest.getEventType());
        EventLogDto.Response response = eventLogService.createEventLog(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID ile event log getir")
    public ResponseEntity<EventLogDto.Response> getEventLogById(@PathVariable Long id) {
        EventLogDto.Response response = eventLogService.getEventLogById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Tüm event log'ları listele")
    public ResponseEntity<Page<EventLogDto.ListResponse>> getAllEventLogs(Pageable pageable) {
        Page<EventLogDto.ListResponse> eventLogs = eventLogService.getAllEventLogs(pageable);
        return ResponseEntity.ok(eventLogs);
    }

    @GetMapping("/type/{eventType}")
    @Operation(summary = "Event tipine göre log'ları getir")
    public ResponseEntity<Page<EventLogDto.ListResponse>> getEventLogsByType(
            @PathVariable String eventType,
            Pageable pageable) {
        Page<EventLogDto.ListResponse> eventLogs = eventLogService.getEventLogsByType(eventType, pageable);
        return ResponseEntity.ok(eventLogs);
    }

    @GetMapping("/service/{sourceService}")
    @Operation(summary = "Servise göre log'ları getir")
    public ResponseEntity<Page<EventLogDto.ListResponse>> getEventLogsByService(
            @PathVariable String sourceService,
            Pageable pageable) {
        Page<EventLogDto.ListResponse> eventLogs = eventLogService.getEventLogsByService(sourceService, pageable);
        return ResponseEntity.ok(eventLogs);
    }

    @GetMapping("/entity")
    @Operation(summary = "Entity'ye göre log'ları getir")
    public ResponseEntity<List<EventLogDto.Response>> getEventLogsByEntity(
            @RequestParam String entityId,
            @RequestParam String entityType) {
        List<EventLogDto.Response> eventLogs = eventLogService.getEventLogsByEntity(entityId, entityType);
        return ResponseEntity.ok(eventLogs);
    }

    @GetMapping("/time-range")
    @Operation(summary = "Zaman aralığına göre log'ları getir")
    public ResponseEntity<Page<EventLogDto.ListResponse>> getEventLogsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            Pageable pageable) {
        Page<EventLogDto.ListResponse> eventLogs = eventLogService.getEventLogsByTimeRange(startTime, endTime, pageable);
        return ResponseEntity.ok(eventLogs);
    }

    @GetMapping("/correlation/{correlationId}")
    @Operation(summary = "Correlation ID'ye göre log'ları getir")
    public ResponseEntity<List<EventLogDto.Response>> getEventLogsByCorrelation(@PathVariable String correlationId) {
        List<EventLogDto.Response> eventLogs = eventLogService.getEventLogsByCorrelation(correlationId);
        return ResponseEntity.ok(eventLogs);
    }
}