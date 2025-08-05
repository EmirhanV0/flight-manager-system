package com.uys.reference.service;

import com.uys.reference.dto.AirlineDto;
import com.uys.reference.entity.Airline;
import com.uys.reference.event.enums.EventType;
import com.uys.reference.event.publisher.EventPublisherService;
import com.uys.reference.mapper.AirlineMapper;
import com.uys.reference.repository.AirlineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Airline Service - Havayolu şirketi iş mantığı katmanı
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AirlineService {

    private final AirlineRepository airlineRepository;
    private final AirlineMapper airlineMapper;
    private final EventPublisherService eventPublisherService;

    /**
     * Yeni airline oluşturur
     * 
     * @param createRequest create request
     * @return oluşturulan airline response
     */
    @CacheEvict(value = "airlines", allEntries = true)
    public AirlineDto.Response createAirline(AirlineDto.CreateRequest createRequest) {
        log.info("Creating airline with code: {}", createRequest.getAirlineCode());

        // Airline code'un benzersiz olduğunu kontrol et
        if (airlineRepository.existsByAirlineCode(createRequest.getAirlineCode())) {
            throw new IllegalArgumentException("Airline with code " + createRequest.getAirlineCode() + " already exists");
        }

        // Airline name'in benzersiz olduğunu kontrol et
        if (airlineRepository.existsByAirlineNameIgnoreCase(createRequest.getAirlineName())) {
            throw new IllegalArgumentException("Airline with name " + createRequest.getAirlineName() + " already exists");
        }

        Airline airline = airlineMapper.toEntity(createRequest);
        Airline savedAirline = airlineRepository.save(airline);
        
        // Publish event to Kafka
        eventPublisherService.publishAirlineEvent(savedAirline, EventType.CREATED);
        
        log.info("Airline created successfully with ID: {}", savedAirline.getId());
        return airlineMapper.toResponse(savedAirline);
    }

    /**
     * ID ile airline getirir
     * 
     * @param id airline ID
     * @return airline response
     */
    @Transactional(readOnly = true)
    public AirlineDto.Response getAirlineById(Long id) {
        log.debug("Getting airline by ID: {}", id);
        
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Airline not found with ID: " + id));
        
        return airlineMapper.toResponse(airline);
    }

    /**
     * Airline code ile airline getirir
     * 
     * @param airlineCode airline code
     * @return airline response
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "airlines", key = "#airlineCode")
    public AirlineDto.Response getAirlineByCode(String airlineCode) {
        log.debug("Getting airline by code: {}", airlineCode);
        
        Airline airline = airlineRepository.findByAirlineCode(airlineCode)
                .orElseThrow(() -> new IllegalArgumentException("Airline not found with code: " + airlineCode));
        
        return airlineMapper.toResponse(airline);
    }

    /**
     * Tüm aktif airline'ları listeler
     * 
     * @return airline listesi
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "airlines", key = "'active'")
    public List<AirlineDto.ListResponse> getAllActiveAirlines() {
        log.debug("Getting all active airlines");
        
        List<Airline> airlines = airlineRepository.findByActiveTrue();
        return airlineMapper.toListResponseList(airlines);
    }

    /**
     * Sayfalı olarak aktif airline'ları listeler
     * 
     * @param pageable sayfalama bilgisi
     * @return sayfalı airline listesi
     */
    @Transactional(readOnly = true)
    public Page<AirlineDto.ListResponse> getActiveAirlines(Pageable pageable) {
        log.debug("Getting active airlines with pagination: {}", pageable);
        
        Page<Airline> airlines = airlineRepository.findByActiveTrue(pageable);
        return airlines.map(airlineMapper::toListResponse);
    }

    /**
     * Airline günceller
     * 
     * @param id airline ID
     * @param updateRequest update request
     * @return güncellenmiş airline response
     */
    @CacheEvict(value = "airlines", allEntries = true)
    public AirlineDto.Response updateAirline(String airlineCode, AirlineDto.UpdateRequest updateRequest) {
        log.info("Updating airline with code: {}", airlineCode);
        
        Airline airline = airlineRepository.findByAirlineCode(airlineCode)
                .orElseThrow(() -> new IllegalArgumentException("Airline not found with code: " + airlineCode));

        // Airline name değişiyorsa benzersizlik kontrolü yap
        if (updateRequest.getAirlineName() != null && 
            !updateRequest.getAirlineName().equalsIgnoreCase(airline.getAirlineName()) &&
            airlineRepository.existsByAirlineNameIgnoreCase(updateRequest.getAirlineName())) {
            throw new IllegalArgumentException("Airline with name " + updateRequest.getAirlineName() + " already exists");
        }

        airlineMapper.updateEntityFromUpdateRequest(updateRequest, airline);
        Airline updatedAirline = airlineRepository.save(airline);
        
        // Publish event to Kafka
        eventPublisherService.publishAirlineEvent(updatedAirline, EventType.UPDATED);
        
        log.info("Airline updated successfully with ID: {}", updatedAirline.getId());
        return airlineMapper.toResponse(updatedAirline);
    }

    /**
     * Airline'ı aktif/pasif yapar
     * 
     * @param id airline ID
     * @param active aktif durumu
     * @return güncellenmiş airline response
     */
    @CacheEvict(value = "airlines", allEntries = true)
    public AirlineDto.Response updateAirlineStatus(Long id, boolean active) {
        log.info("Updating airline status with ID: {} to active: {}", id, active);
        
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Airline not found with ID: " + id));

        if (active) {
            airline.activate();
        } else {
            airline.deactivate();
        }

        Airline updatedAirline = airlineRepository.save(airline);
        
        // Publish event to Kafka
        eventPublisherService.publishAirlineEvent(updatedAirline, EventType.STATUS_CHANGED);
        
        log.info("Airline status updated successfully with ID: {}", updatedAirline.getId());
        return airlineMapper.toResponse(updatedAirline);
    }

    /**
     * Airline siler (soft delete)
     * 
     * @param id airline ID
     */
    @CacheEvict(value = "airlines", allEntries = true)
    public void deleteAirline(String airlineCode) {
        log.info("Deleting airline with code: {}", airlineCode);
        
        Airline airline = airlineRepository.findByAirlineCode(airlineCode)
                .orElseThrow(() -> new IllegalArgumentException("Airline not found with code: " + airlineCode));

        airline.deactivate();
        Airline deletedAirline = airlineRepository.save(airline);
        
        // Publish event to Kafka
        eventPublisherService.publishAirlineEvent(deletedAirline, EventType.DELETED);
        
        log.info("Airline deleted successfully with code: {}", airlineCode);
    }

    /**
     * Country ile airline'ları arar
     * 
     * @param country country
     * @return airline listesi
     */
    @Transactional(readOnly = true)
    public List<AirlineDto.ListResponse> getAirlinesByCountry(String country) {
        log.debug("Getting airlines by country: {}", country);
        
        List<Airline> airlines = airlineRepository.findByCountryAndActiveTrue(country);
        return airlineMapper.toListResponseList(airlines);
    }

    /**
     * City ile airline'ları arar
     * 
     * @param city city
     * @return airline listesi
     */
    @Transactional(readOnly = true)
    public List<AirlineDto.ListResponse> getAirlinesByCity(String city) {
        log.debug("Getting airlines by city: {}", city);
        
        List<Airline> airlines = airlineRepository.findByCityAndActiveTrue(city);
        return airlineMapper.toListResponseList(airlines);
    }

    /**
     * Airline name ile arama yapar
     * 
     * @param airlineName airline name
     * @return airline listesi
     */
    @Transactional(readOnly = true)
    public List<AirlineDto.ListResponse> searchAirlinesByName(String airlineName) {
        log.debug("Searching airlines by name: {}", airlineName);
        
        List<Airline> airlines = airlineRepository.findByAirlineNameContainingIgnoreCaseAndActiveTrue(airlineName);
        return airlineMapper.toListResponseList(airlines);
    }

    /**
     * Airline code ile arama yapar
     * 
     * @param airlineCode airline code
     * @return airline listesi
     */
    @Transactional(readOnly = true)
    public List<AirlineDto.ListResponse> searchAirlinesByCode(String airlineCode) {
        log.debug("Searching airlines by code: {}", airlineCode);
        
        List<Airline> airlines = airlineRepository.findByAirlineCodeContainingIgnoreCaseAndActiveTrue(airlineCode);
        return airlineMapper.toListResponseList(airlines);
    }

    /**
     * Airline status'ünü değiştirir
     */
    @CacheEvict(value = "airlines", allEntries = true)
    public AirlineDto.Response changeAirlineStatus(String airlineCode, boolean active) {
        log.info("Changing airline status: {} to {}", airlineCode, active);
        
        Airline airline = airlineRepository.findByAirlineCode(airlineCode)
                .orElseThrow(() -> new IllegalArgumentException("Airline not found: " + airlineCode));
        
        airline.setActive(active);
        Airline savedAirline = airlineRepository.save(airline);
        
        // Publish event to Kafka
        eventPublisherService.publishAirlineEvent(savedAirline, EventType.STATUS_CHANGED);
        
        log.info("Airline status changed successfully: {}", airlineCode);
        return airlineMapper.toResponse(savedAirline);
    }

    /**
     * Tüm airline'ları sayfalı şekilde getirir
     */
    @Transactional(readOnly = true)
    public Page<AirlineDto.Response> getAllAirlines(Pageable pageable) {
        log.debug("Getting all airlines with pagination: {}", pageable);
        
        Page<Airline> airlines = airlineRepository.findAll(pageable);
        return airlines.map(airlineMapper::toResponse);
    }

    /**
     * Airline'ları arar (genel arama)
     */
    @Transactional(readOnly = true)
    public List<AirlineDto.ListResponse> searchAirlines(String searchTerm) {
        log.debug("Searching airlines with term: {}", searchTerm);
        
        List<Airline> airlines = airlineRepository.findByAirlineNameContainingIgnoreCaseOrCountryContainingIgnoreCase(searchTerm, searchTerm);
        return airlineMapper.toListResponseList(airlines);
    }
} 