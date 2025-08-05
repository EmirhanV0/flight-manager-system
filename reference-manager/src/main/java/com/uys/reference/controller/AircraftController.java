package com.uys.reference.controller;

import com.uys.reference.dto.AircraftDto;
import com.uys.reference.service.AircraftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Aircraft Controller - Uçak REST API
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@RestController
@RequestMapping("/aircraft")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Aircraft", description = "Uçak yönetimi API'leri")
public class AircraftController {

    private final AircraftService aircraftService;

    /**
     * Yeni aircraft oluşturur
     * 
     * @param createRequest create request
     * @return oluşturulan aircraft
     */
    @PostMapping
    @Operation(summary = "Yeni aircraft oluştur", description = "Yeni bir uçak oluşturur")
    public ResponseEntity<AircraftDto.Response> createAircraft(
            @Valid @RequestBody AircraftDto.CreateRequest createRequest) {
        log.info("Creating aircraft with registration: {}", createRequest.getRegistration());
        AircraftDto.Response response = aircraftService.createAircraft(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * ID ile aircraft getirir
     * 
     * @param id aircraft ID
     * @return aircraft
     */
    @GetMapping("/{id}")
    @Operation(summary = "ID ile aircraft getir", description = "Belirtilen ID'ye sahip aircraft'ı getirir")
    public ResponseEntity<AircraftDto.Response> getAircraftById(
            @Parameter(description = "Aircraft ID") @PathVariable Long id) {
        log.debug("Getting aircraft by ID: {}", id);
        AircraftDto.Response response = aircraftService.getAircraftById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Registration ile aircraft getirir
     * 
     * @param registration registration
     * @return aircraft
     */
    @GetMapping("/registration/{registration}")
    @Operation(summary = "Registration ile aircraft getir", description = "Belirtilen registration'a sahip aircraft'ı getirir")
    public ResponseEntity<AircraftDto.Response> getAircraftByRegistration(
            @Parameter(description = "Aircraft registration") @PathVariable String registration) {
        log.debug("Getting aircraft by registration: {}", registration);
        AircraftDto.Response response = aircraftService.getAircraftByRegistration(registration);
        return ResponseEntity.ok(response);
    }

    /**
     * Tüm aktif aircraft'ları listeler
     * 
     * @return aircraft listesi
     */
    @GetMapping
    @Operation(summary = "Tüm aktif aircraft'ları listele", description = "Aktif tüm uçakları listeler")
    public ResponseEntity<List<AircraftDto.ListResponse>> getAllActiveAircraft() {
        log.debug("Getting all active aircraft");
        List<AircraftDto.ListResponse> response = aircraftService.getAllActiveAircraft();
        return ResponseEntity.ok(response);
    }

    /**
     * Sayfalı olarak aktif aircraft'ları listeler
     * 
     * @param pageable sayfalama bilgisi
     * @return sayfalı aircraft listesi
     */
    @GetMapping("/page")
    @Operation(summary = "Sayfalı aircraft listesi", description = "Sayfalı olarak aktif aircraft'ları listeler")
    public ResponseEntity<Page<AircraftDto.ListResponse>> getActiveAircraft(Pageable pageable) {
        log.debug("Getting active aircraft with pagination: {}", pageable);
        Page<AircraftDto.ListResponse> response = aircraftService.getActiveAircraft(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Aircraft günceller
     * 
     * @param id aircraft ID
     * @param updateRequest update request
     * @return güncellenmiş aircraft
     */
    @PutMapping("/{id}")
    @Operation(summary = "Aircraft güncelle", description = "Belirtilen ID'ye sahip aircraft'ı günceller")
    public ResponseEntity<AircraftDto.Response> updateAircraft(
            @Parameter(description = "Aircraft ID") @PathVariable Long id,
            @Valid @RequestBody AircraftDto.UpdateRequest updateRequest) {
        log.info("Updating aircraft with ID: {}", id);
        AircraftDto.Response response = aircraftService.updateAircraft(id, updateRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Aircraft'ı aktif/pasif yapar
     * 
     * @param id aircraft ID
     * @param active aktif durumu
     * @return güncellenmiş aircraft
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Aircraft durumunu güncelle", description = "Aircraft'ın aktif/pasif durumunu günceller")
    public ResponseEntity<AircraftDto.Response> updateAircraftStatus(
            @Parameter(description = "Aircraft ID") @PathVariable Long id,
            @Parameter(description = "Aktif durumu") @RequestParam boolean active) {
        log.info("Updating aircraft status with ID: {} to active: {}", id, active);
        AircraftDto.Response response = aircraftService.updateAircraftStatus(id, active);
        return ResponseEntity.ok(response);
    }

    /**
     * Aircraft siler
     * 
     * @param id aircraft ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Aircraft sil", description = "Belirtilen ID'ye sahip aircraft'ı siler (soft delete)")
    public ResponseEntity<Void> deleteAircraft(
            @Parameter(description = "Aircraft ID") @PathVariable Long id) {
        log.info("Deleting aircraft with ID: {}", id);
        aircraftService.deleteAircraft(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Airline ID ile aircraft'ları listeler
     * 
     * @param airlineId airline ID
     * @return aircraft listesi
     */
    @GetMapping("/airline/{airlineId}")
    @Operation(summary = "Airline ID ile aircraft ara", description = "Belirtilen airline'a ait aircraft'ları listeler")
    public ResponseEntity<List<AircraftDto.ListResponse>> getAircraftByAirlineId(
            @Parameter(description = "Airline ID") @PathVariable Long airlineId) {
        log.debug("Getting aircraft by airline ID: {}", airlineId);
        List<AircraftDto.ListResponse> response = aircraftService.getAircraftByAirlineId(airlineId);
        return ResponseEntity.ok(response);
    }

    /**
     * Airline code ile aircraft'ları listeler
     * 
     * @param airlineCode airline code
     * @return aircraft listesi
     */
    @GetMapping("/airline-code/{airlineCode}")
    @Operation(summary = "Airline code ile aircraft ara", description = "Belirtilen airline code'a ait aircraft'ları listeler")
    public ResponseEntity<List<AircraftDto.ListResponse>> getAircraftByAirlineCode(
            @Parameter(description = "Airline code") @PathVariable String airlineCode) {
        log.debug("Getting aircraft by airline code: {}", airlineCode);
        List<AircraftDto.ListResponse> response = aircraftService.getAircraftByAirlineCode(airlineCode);
        return ResponseEntity.ok(response);
    }

    /**
     * Aircraft type ile aircraft'ları listeler
     * 
     * @param aircraftType aircraft type
     * @return aircraft listesi
     */
    @GetMapping("/type/{aircraftType}")
    @Operation(summary = "Type ile aircraft ara", description = "Belirtilen type'a sahip aircraft'ları listeler")
    public ResponseEntity<List<AircraftDto.ListResponse>> getAircraftByType(
            @Parameter(description = "Aircraft type") @PathVariable String aircraftType) {
        log.debug("Getting aircraft by type: {}", aircraftType);
        List<AircraftDto.ListResponse> response = aircraftService.getAircraftByType(aircraftType);
        return ResponseEntity.ok(response);
    }

    /**
     * Manufacturer ile aircraft'ları listeler
     * 
     * @param manufacturer manufacturer
     * @return aircraft listesi
     */
    @GetMapping("/manufacturer/{manufacturer}")
    @Operation(summary = "Manufacturer ile aircraft ara", description = "Belirtilen manufacturer'a sahip aircraft'ları listeler")
    public ResponseEntity<List<AircraftDto.ListResponse>> getAircraftByManufacturer(
            @Parameter(description = "Manufacturer") @PathVariable String manufacturer) {
        log.debug("Getting aircraft by manufacturer: {}", manufacturer);
        List<AircraftDto.ListResponse> response = aircraftService.getAircraftByManufacturer(manufacturer);
        return ResponseEntity.ok(response);
    }

    /**
     * Registration ile arama yapar
     * 
     * @param registration registration
     * @return aircraft listesi
     */
    @GetMapping("/search/registration")
    @Operation(summary = "Registration ile aircraft ara", description = "Registration ile arama yapar")
    public ResponseEntity<List<AircraftDto.ListResponse>> searchAircraftByRegistration(
            @Parameter(description = "Registration") @RequestParam String registration) {
        log.debug("Searching aircraft by registration: {}", registration);
        List<AircraftDto.ListResponse> response = aircraftService.searchAircraftByRegistration(registration);
        return ResponseEntity.ok(response);
    }

    /**
     * Aircraft type ile arama yapar
     * 
     * @param aircraftType aircraft type
     * @return aircraft listesi
     */
    @GetMapping("/search/type")
    @Operation(summary = "Type ile aircraft ara", description = "Aircraft type ile arama yapar")
    public ResponseEntity<List<AircraftDto.ListResponse>> searchAircraftByType(
            @Parameter(description = "Aircraft type") @RequestParam String aircraftType) {
        log.debug("Searching aircraft by type: {}", aircraftType);
        List<AircraftDto.ListResponse> response = aircraftService.searchAircraftByType(aircraftType);
        return ResponseEntity.ok(response);
    }

    /**
     * Manufacturer ile arama yapar
     * 
     * @param manufacturer manufacturer
     * @return aircraft listesi
     */
    @GetMapping("/search/manufacturer")
    @Operation(summary = "Manufacturer ile aircraft ara", description = "Manufacturer ile arama yapar")
    public ResponseEntity<List<AircraftDto.ListResponse>> searchAircraftByManufacturer(
            @Parameter(description = "Manufacturer") @RequestParam String manufacturer) {
        log.debug("Searching aircraft by manufacturer: {}", manufacturer);
        List<AircraftDto.ListResponse> response = aircraftService.searchAircraftByManufacturer(manufacturer);
        return ResponseEntity.ok(response);
    }

    /**
     * Capacity aralığında aircraft'ları arar
     * 
     * @param minCapacity minimum capacity
     * @param maxCapacity maximum capacity
     * @return aircraft listesi
     */
    @GetMapping("/search/capacity")
    @Operation(summary = "Capacity aralığında aircraft ara", description = "Capacity aralığında aircraft'ları arar")
    public ResponseEntity<List<AircraftDto.ListResponse>> getAircraftByCapacityRange(
            @Parameter(description = "Minimum capacity") @RequestParam Integer minCapacity,
            @Parameter(description = "Maximum capacity") @RequestParam Integer maxCapacity) {
        log.debug("Getting aircraft by capacity range: {} - {}", minCapacity, maxCapacity);
        List<AircraftDto.ListResponse> response = aircraftService.getAircraftByCapacityRange(minCapacity, maxCapacity);
        return ResponseEntity.ok(response);
    }
} 