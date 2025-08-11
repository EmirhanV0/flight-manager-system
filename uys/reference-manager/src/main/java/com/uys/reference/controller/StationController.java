package com.uys.reference.controller;

import com.uys.reference.dto.StationDto;
import com.uys.reference.service.StationService;
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
 * Station Controller - Havaalanı REST API
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@RestController
@RequestMapping("/stations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Station", description = "Havaalanı yönetimi API'leri")
public class StationController {

    private final StationService stationService;

    /**
     * Yeni station oluşturur
     * 
     * @param createRequest create request
     * @return oluşturulan station
     */
    @PostMapping
    @Operation(summary = "Yeni station oluştur", description = "Yeni bir havaalanı oluşturur")
    public ResponseEntity<StationDto.Response> createStation(
            @Valid @RequestBody StationDto.CreateRequest createRequest) {
        log.info("Creating station with code: {}", createRequest.getStationCode());
        StationDto.Response response = stationService.createStation(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * ID ile station getirir
     * 
     * @param id station ID
     * @return station
     */
    @GetMapping("/{id}")
    @Operation(summary = "ID ile station getir", description = "Belirtilen ID'ye sahip station'ı getirir")
    public ResponseEntity<StationDto.Response> getStationById(
            @Parameter(description = "Station ID") @PathVariable Long id) {
        log.debug("Getting station by ID: {}", id);
        StationDto.Response response = stationService.getStationById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Station code ile station getirir
     * 
     * @param stationCode station code
     * @return station
     */
    @GetMapping("/code/{stationCode}")
    @Operation(summary = "Code ile station getir", description = "Belirtilen code'a sahip station'ı getirir")
    public ResponseEntity<StationDto.Response> getStationByCode(
            @Parameter(description = "Station code") @PathVariable String stationCode) {
        log.debug("Getting station by code: {}", stationCode);
        StationDto.Response response = stationService.getStationByCode(stationCode);
        return ResponseEntity.ok(response);
    }

    /**
     * Tüm aktif station'ları listeler
     * 
     * @return station listesi
     */
    @GetMapping
    @Operation(summary = "Tüm aktif station'ları listele", description = "Aktif tüm havaalanlarını listeler")
    public ResponseEntity<List<StationDto.ListResponse>> getAllActiveStations() {
        log.debug("Getting all active stations");
        List<StationDto.ListResponse> response = stationService.getAllActiveStations();
        return ResponseEntity.ok(response);
    }

    /**
     * Sayfalı olarak aktif station'ları listeler
     * 
     * @param pageable sayfalama bilgisi
     * @return sayfalı station listesi
     */
    @GetMapping("/page")
    @Operation(summary = "Sayfalı station listesi", description = "Sayfalı olarak aktif station'ları listeler")
    public ResponseEntity<Page<StationDto.ListResponse>> getActiveStations(Pageable pageable) {
        log.debug("Getting active stations with pagination: {}", pageable);
        Page<StationDto.ListResponse> response = stationService.getActiveStations(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Station günceller
     * 
     * @param id station ID
     * @param updateRequest update request
     * @return güncellenmiş station
     */
    @PutMapping("/{id}")
    @Operation(summary = "Station güncelle", description = "Belirtilen ID'ye sahip station'ı günceller")
    public ResponseEntity<StationDto.Response> updateStation(
            @Parameter(description = "Station ID") @PathVariable Long id,
            @Valid @RequestBody StationDto.UpdateRequest updateRequest) {
        log.info("Updating station with ID: {}", id);
        StationDto.Response response = stationService.updateStation(id, updateRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Station'ı aktif/pasif yapar
     * 
     * @param id station ID
     * @param active aktif durumu
     * @return güncellenmiş station
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Station durumunu güncelle", description = "Station'ın aktif/pasif durumunu günceller")
    public ResponseEntity<StationDto.Response> updateStationStatus(
            @Parameter(description = "Station ID") @PathVariable Long id,
            @Parameter(description = "Aktif durumu") @RequestParam boolean active) {
        log.info("Updating station status with ID: {} to active: {}", id, active);
        StationDto.Response response = stationService.updateStationStatus(id, active);
        return ResponseEntity.ok(response);
    }

    /**
     * Station siler
     * 
     * @param id station ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Station sil", description = "Belirtilen ID'ye sahip station'ı siler (soft delete)")
    public ResponseEntity<Void> deleteStation(
            @Parameter(description = "Station ID") @PathVariable Long id) {
        log.info("Deleting station with ID: {}", id);
        stationService.deleteStation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Country ile station'ları listeler
     * 
     * @param country country
     * @return station listesi
     */
    @GetMapping("/country/{country}")
    @Operation(summary = "Country ile station ara", description = "Belirtilen ülkedeki station'ları listeler")
    public ResponseEntity<List<StationDto.ListResponse>> getStationsByCountry(
            @Parameter(description = "Country") @PathVariable String country) {
        log.debug("Getting stations by country: {}", country);
        List<StationDto.ListResponse> response = stationService.getStationsByCountry(country);
        return ResponseEntity.ok(response);
    }

    /**
     * City ile station'ları listeler
     * 
     * @param city city
     * @return station listesi
     */
    @GetMapping("/city/{city}")
    @Operation(summary = "City ile station ara", description = "Belirtilen şehirdeki station'ları listeler")
    public ResponseEntity<List<StationDto.ListResponse>> getStationsByCity(
            @Parameter(description = "City") @PathVariable String city) {
        log.debug("Getting stations by city: {}", city);
        List<StationDto.ListResponse> response = stationService.getStationsByCity(city);
        return ResponseEntity.ok(response);
    }

    /**
     * Timezone ile station'ları listeler
     * 
     * @param timezone timezone
     * @return station listesi
     */
    @GetMapping("/timezone/{timezone}")
    @Operation(summary = "Timezone ile station ara", description = "Belirtilen timezone'daki station'ları listeler")
    public ResponseEntity<List<StationDto.ListResponse>> getStationsByTimezone(
            @Parameter(description = "Timezone") @PathVariable String timezone) {
        log.debug("Getting stations by timezone: {}", timezone);
        List<StationDto.ListResponse> response = stationService.getStationsByTimezone(timezone);
        return ResponseEntity.ok(response);
    }

    /**
     * Station name ile arama yapar
     * 
     * @param stationName station name
     * @return station listesi
     */
    @GetMapping("/search/name")
    @Operation(summary = "Name ile station ara", description = "Station name ile arama yapar")
    public ResponseEntity<List<StationDto.ListResponse>> searchStationsByName(
            @Parameter(description = "Station name") @RequestParam String stationName) {
        log.debug("Searching stations by name: {}", stationName);
        List<StationDto.ListResponse> response = stationService.searchStationsByName(stationName);
        return ResponseEntity.ok(response);
    }

    /**
     * Station code ile arama yapar
     * 
     * @param stationCode station code
     * @return station listesi
     */
    @GetMapping("/search/code")
    @Operation(summary = "Code ile station ara", description = "Station code ile arama yapar")
    public ResponseEntity<List<StationDto.ListResponse>> searchStationsByCode(
            @Parameter(description = "Station code") @RequestParam String stationCode) {
        log.debug("Searching stations by code: {}", stationCode);
        List<StationDto.ListResponse> response = stationService.searchStationsByCode(stationCode);
        return ResponseEntity.ok(response);
    }

    /**
     * City ile arama yapar
     * 
     * @param city city
     * @return station listesi
     */
    @GetMapping("/search/city")
    @Operation(summary = "City ile station ara", description = "City ile arama yapar")
    public ResponseEntity<List<StationDto.ListResponse>> searchStationsByCity(
            @Parameter(description = "City") @RequestParam String city) {
        log.debug("Searching stations by city: {}", city);
        List<StationDto.ListResponse> response = stationService.searchStationsByCity(city);
        return ResponseEntity.ok(response);
    }

    /**
     * Country ile arama yapar
     * 
     * @param country country
     * @return station listesi
     */
    @GetMapping("/search/country")
    @Operation(summary = "Country ile station ara", description = "Country ile arama yapar")
    public ResponseEntity<List<StationDto.ListResponse>> searchStationsByCountry(
            @Parameter(description = "Country") @RequestParam String country) {
        log.debug("Searching stations by country: {}", country);
        List<StationDto.ListResponse> response = stationService.searchStationsByCountry(country);
        return ResponseEntity.ok(response);
    }

    /**
     * Belirli bir bölgedeki station'ları arar
     * 
     * @param minLat minimum latitude
     * @param maxLat maximum latitude
     * @param minLon minimum longitude
     * @param maxLon maximum longitude
     * @return station listesi
     */
    @GetMapping("/search/location")
    @Operation(summary = "Location ile station ara", description = "Belirli bir bölgedeki station'ları arar")
    public ResponseEntity<List<StationDto.ListResponse>> getStationsByLocationRange(
            @Parameter(description = "Minimum latitude") @RequestParam Double minLat,
            @Parameter(description = "Maximum latitude") @RequestParam Double maxLat,
            @Parameter(description = "Minimum longitude") @RequestParam Double minLon,
            @Parameter(description = "Maximum longitude") @RequestParam Double maxLon) {
        log.debug("Getting stations by location range: lat({}-{}), lon({}-{})", minLat, maxLat, minLon, maxLon);
        List<StationDto.ListResponse> response = stationService.getStationsByLocationRange(minLat, maxLat, minLon, maxLon);
        return ResponseEntity.ok(response);
    }
} 