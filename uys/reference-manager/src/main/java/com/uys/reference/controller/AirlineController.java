package com.uys.reference.controller;

import com.uys.reference.dto.AirlineDto;
import com.uys.reference.dto.ErrorResponse;
import com.uys.reference.service.AirlineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
 * Airline Controller - Havayolu şirketi REST API
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@RestController
@RequestMapping("/airlines")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Airline", description = "Havayolu şirketi yönetimi API'leri")
public class AirlineController {

    private final AirlineService airlineService;

    /**
     * Yeni airline oluşturur
     * 
     * @param createRequest create request
     * @return oluşturulan airline
     */
    @PostMapping
    @Operation(
        summary = "Yeni airline oluştur", 
        description = "Yeni bir havayolu şirketi oluşturur. Airline code benzersiz olmalıdır."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Airline başarıyla oluşturuldu",
            content = @Content(schema = @Schema(implementation = AirlineDto.Response.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Geçersiz veri veya duplicate airline code",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public ResponseEntity<AirlineDto.Response> createAirline(
            @Parameter(description = "Oluşturulacak airline bilgileri", required = true)
            @Valid @RequestBody AirlineDto.CreateRequest createRequest) {
        log.info("Creating airline with code: {}", createRequest.getAirlineCode());
        AirlineDto.Response response = airlineService.createAirline(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * ID ile airline getirir
     * 
     * @param id airline ID
     * @return airline
     */
    @GetMapping("/{id}")
    @Operation(summary = "ID ile airline getir", description = "Belirtilen ID'ye sahip airline'ı getirir")
    public ResponseEntity<AirlineDto.Response> getAirlineById(
            @Parameter(description = "Airline ID") @PathVariable Long id) {
        log.debug("Getting airline by ID: {}", id);
        AirlineDto.Response response = airlineService.getAirlineById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Airline code ile airline getirir
     * 
     * @param airlineCode airline code
     * @return airline
     */
    @GetMapping("/code/{airlineCode}")
    @Operation(summary = "Code ile airline getir", description = "Belirtilen code'a sahip airline'ı getirir")
    public ResponseEntity<AirlineDto.Response> getAirlineByCode(
            @Parameter(description = "Airline code") @PathVariable String airlineCode) {
        log.debug("Getting airline by code: {}", airlineCode);
        AirlineDto.Response response = airlineService.getAirlineByCode(airlineCode);
        return ResponseEntity.ok(response);
    }

    /**
     * Tüm aktif airline'ları listeler
     * 
     * @return airline listesi
     */
    @GetMapping
    @Operation(summary = "Tüm aktif airline'ları listele", description = "Aktif tüm havayolu şirketlerini listeler")
    public ResponseEntity<List<AirlineDto.ListResponse>> getAllActiveAirlines() {
        log.debug("Getting all active airlines");
        List<AirlineDto.ListResponse> response = airlineService.getAllActiveAirlines();
        return ResponseEntity.ok(response);
    }

    /**
     * Sayfalı olarak aktif airline'ları listeler
     * 
     * @param pageable sayfalama bilgisi
     * @return sayfalı airline listesi
     */
    @GetMapping("/page")
    @Operation(summary = "Sayfalı airline listesi", description = "Sayfalı olarak aktif airline'ları listeler")
    public ResponseEntity<Page<AirlineDto.ListResponse>> getActiveAirlines(Pageable pageable) {
        log.debug("Getting active airlines with pagination: {}", pageable);
        Page<AirlineDto.ListResponse> response = airlineService.getActiveAirlines(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Airline günceller
     * 
     * @param id airline ID
     * @param updateRequest update request
     * @return güncellenmiş airline
     */
    @PutMapping("/{airlineCode}")
    @Operation(summary = "Airline güncelle", description = "Belirtilen code'a sahip airline'ı günceller")
    public ResponseEntity<AirlineDto.Response> updateAirline(
            @Parameter(description = "Airline Code") @PathVariable String airlineCode,
            @Valid @RequestBody AirlineDto.UpdateRequest updateRequest) {
        log.info("Updating airline with code: {}", airlineCode);
        AirlineDto.Response response = airlineService.updateAirline(airlineCode, updateRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Airline'ı aktif/pasif yapar
     * 
     * @param id airline ID
     * @param active aktif durumu
     * @return güncellenmiş airline
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Airline durumunu güncelle", description = "Airline'ın aktif/pasif durumunu günceller")
    public ResponseEntity<AirlineDto.Response> updateAirlineStatus(
            @Parameter(description = "Airline ID") @PathVariable Long id,
            @Parameter(description = "Aktif durumu") @RequestParam boolean active) {
        log.info("Updating airline status with ID: {} to active: {}", id, active);
        AirlineDto.Response response = airlineService.updateAirlineStatus(id, active);
        return ResponseEntity.ok(response);
    }

    /**
     * Airline siler
     * 
     * @param id airline ID
     * @return no content
     */
    @DeleteMapping("/{airlineCode}")
    @Operation(summary = "Airline sil", description = "Belirtilen code'a sahip airline'ı siler (soft delete)")
    public ResponseEntity<Void> deleteAirline(
            @Parameter(description = "Airline Code") @PathVariable String airlineCode) {
        log.info("Deleting airline with code: {}", airlineCode);
        airlineService.deleteAirline(airlineCode);
        return ResponseEntity.noContent().build();
    }

    /**
     * Country ile airline'ları arar
     * 
     * @param country country
     * @return airline listesi
     */
    @GetMapping("/country/{country}")
    @Operation(summary = "Country ile airline ara", description = "Belirtilen ülkedeki airline'ları listeler")
    public ResponseEntity<List<AirlineDto.ListResponse>> getAirlinesByCountry(
            @Parameter(description = "Country") @PathVariable String country) {
        log.debug("Getting airlines by country: {}", country);
        List<AirlineDto.ListResponse> response = airlineService.getAirlinesByCountry(country);
        return ResponseEntity.ok(response);
    }

    /**
     * City ile airline'ları arar
     * 
     * @param city city
     * @return airline listesi
     */
    @GetMapping("/city/{city}")
    @Operation(summary = "City ile airline ara", description = "Belirtilen şehirdeki airline'ları listeler")
    public ResponseEntity<List<AirlineDto.ListResponse>> getAirlinesByCity(
            @Parameter(description = "City") @PathVariable String city) {
        log.debug("Getting airlines by city: {}", city);
        List<AirlineDto.ListResponse> response = airlineService.getAirlinesByCity(city);
        return ResponseEntity.ok(response);
    }

    /**
     * Airline name ile arama yapar
     * 
     * @param airlineName airline name
     * @return airline listesi
     */
    @GetMapping("/search/name")
    @Operation(summary = "Name ile airline ara", description = "Airline name ile arama yapar")
    public ResponseEntity<List<AirlineDto.ListResponse>> searchAirlinesByName(
            @Parameter(description = "Airline name") @RequestParam String airlineName) {
        log.debug("Searching airlines by name: {}", airlineName);
        List<AirlineDto.ListResponse> response = airlineService.searchAirlinesByName(airlineName);
        return ResponseEntity.ok(response);
    }

    /**
     * Airline code ile arama yapar
     * 
     * @param airlineCode airline code
     * @return airline listesi
     */
    @GetMapping("/search/code")
    @Operation(summary = "Code ile airline ara", description = "Airline code ile arama yapar")
    public ResponseEntity<List<AirlineDto.ListResponse>> searchAirlinesByCode(
            @Parameter(description = "Airline code") @RequestParam String airlineCode) {
        log.debug("Searching airlines by code: {}", airlineCode);
        List<AirlineDto.ListResponse> response = airlineService.searchAirlinesByCode(airlineCode);
        return ResponseEntity.ok(response);
    }

    /**
     * Genel arama yapar
     * 
     * @param searchTerm arama terimi
     * @return airline listesi
     */
    @GetMapping("/search")
    @Operation(summary = "Genel arama", description = "Airline name veya country ile arama yapar")
    public ResponseEntity<List<AirlineDto.ListResponse>> searchAirlines(
            @Parameter(description = "Arama terimi") @RequestParam String searchTerm) {
        log.debug("Searching airlines with term: {}", searchTerm);
        List<AirlineDto.ListResponse> response = airlineService.searchAirlines(searchTerm);
        return ResponseEntity.ok(response);
    }

    /**
     * Aktif airline sayısını getirir
     * 
     * @return aktif airline sayısı
     */
    @GetMapping("/count/active")
    @Operation(summary = "Aktif airline sayısı", description = "Aktif airline sayısını getirir")
    public ResponseEntity<Long> getActiveAirlineCount() {
        log.debug("Getting active airline count");
        long count = airlineService.getActiveAirlineCount();
        return ResponseEntity.ok(count);
    }

    /**
     * Pasif airline sayısını getirir
     * 
     * @return pasif airline sayısı
     */
    @GetMapping("/count/inactive")
    @Operation(summary = "Pasif airline sayısı", description = "Pasif airline sayısını getirir")
    public ResponseEntity<Long> getInactiveAirlineCount() {
        log.debug("Getting inactive airline count");
        long count = airlineService.getInactiveAirlineCount();
        return ResponseEntity.ok(count);
    }

    /**
     * Country'e göre sıralı airline listesi
     * 
     * @param country country
     * @return airline listesi
     */
    @GetMapping("/country/{country}/ordered")
    @Operation(summary = "Country'e göre sıralı airline listesi", description = "Belirtilen ülkedeki airline'ları sıralı listeler")
    public ResponseEntity<List<AirlineDto.ListResponse>> getAirlinesByCountryOrdered(
            @Parameter(description = "Country") @PathVariable String country) {
        log.debug("Getting airlines by country ordered: {}", country);
        List<AirlineDto.ListResponse> response = airlineService.getAirlinesByCountryOrdered(country);
        return ResponseEntity.ok(response);
    }
} 