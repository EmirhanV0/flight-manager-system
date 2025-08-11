package com.uys.flight.controller;

import com.uys.flight.dto.FlightDto;
import com.uys.flight.enums.FlightStatus;
import com.uys.flight.service.FlightService;
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
 * Flight Controller
 */
@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flight", description = "Uçuş yönetimi API'leri")
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    @Operation(summary = "Yeni uçuş oluştur")
    public ResponseEntity<FlightDto.Response> createFlight(@Valid @RequestBody FlightDto.CreateRequest createRequest) {
        log.info("Creating flight: {}", createRequest.getFlightNumber());
        FlightDto.Response response = flightService.createFlight(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID ile uçuş getir")
    public ResponseEntity<FlightDto.Response> getFlightById(@PathVariable Long id) {
        FlightDto.Response response = flightService.getFlightById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/number/{flightNumber}")
    @Operation(summary = "Uçuş numarası ile uçuş getir")
    public ResponseEntity<FlightDto.Response> getFlightByNumber(@PathVariable String flightNumber) {
        return flightService.getFlightByNumber(flightNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Uçuş güncelle")
    public ResponseEntity<FlightDto.Response> updateFlight(@PathVariable Long id, 
                                                          @Valid @RequestBody FlightDto.UpdateRequest updateRequest) {
        FlightDto.Response response = flightService.updateFlight(id, updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Uçuş sil")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Tüm uçuşları listele")
    public ResponseEntity<Page<FlightDto.Response>> getAllFlights(Pageable pageable) {
        Page<FlightDto.Response> flights = flightService.getAllFlights(pageable);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/airline/{airlineCode}")
    @Operation(summary = "Havayolu şirketine göre uçuşları getir")
    public ResponseEntity<List<FlightDto.ListResponse>> getFlightsByAirline(@PathVariable String airlineCode) {
        List<FlightDto.ListResponse> flights = flightService.getFlightsByAirline(airlineCode);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/route")
    @Operation(summary = "Rota göre uçuşları getir")
    public ResponseEntity<List<FlightDto.ListResponse>> getFlightsByRoute(
            @RequestParam String departure,
            @RequestParam String arrival) {
        List<FlightDto.ListResponse> flights = flightService.getFlightsByRoute(departure, arrival);
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/time-range")
    @Operation(summary = "Zaman aralığına göre uçuşları getir")
    public ResponseEntity<List<FlightDto.ListResponse>> getFlightsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<FlightDto.ListResponse> flights = flightService.getFlightsByTimeRange(startTime, endTime);
        return ResponseEntity.ok(flights);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Uçuş durumunu güncelle")
    public ResponseEntity<FlightDto.Response> updateFlightStatus(@PathVariable Long id,
                                                                @RequestParam FlightStatus status) {
        FlightDto.Response response = flightService.updateFlightStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/delay")
    @Operation(summary = "Uçuşu geciktir")
    public ResponseEntity<FlightDto.Response> delayFlight(@PathVariable Long id,
                                                         @RequestParam int delayMinutes) {
        FlightDto.Response response = flightService.delayFlight(id, delayMinutes);
        return ResponseEntity.ok(response);
    }
}