package com.uys.reference.service;

import com.uys.reference.dto.AircraftDto;
import com.uys.reference.entity.Aircraft;
import com.uys.reference.entity.Airline;
import com.uys.reference.event.enums.EventType;
import com.uys.reference.event.publisher.EventPublisherService;
import com.uys.reference.mapper.AircraftMapper;
import com.uys.reference.repository.AircraftRepository;
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
 * Aircraft Service - Uçak iş mantığı katmanı
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AircraftService {

    private final AircraftRepository aircraftRepository;
    private final AirlineRepository airlineRepository;
    private final AircraftMapper aircraftMapper;
    private final EventPublisherService eventPublisherService;

    /**
     * Yeni aircraft oluşturur
     * 
     * @param createRequest create request
     * @return oluşturulan aircraft response
     */
    @CacheEvict(value = "aircraft", allEntries = true)
    public AircraftDto.Response createAircraft(AircraftDto.CreateRequest createRequest) {
        log.info("Creating aircraft with registration: {}", createRequest.getRegistration());

        // Registration'ın benzersiz olduğunu kontrol et
        if (aircraftRepository.existsByRegistration(createRequest.getRegistration())) {
            throw new IllegalArgumentException("Aircraft with registration " + createRequest.getRegistration() + " already exists");
        }

        // Airline'ın var olduğunu kontrol et
        Airline airline = airlineRepository.findById(createRequest.getAirlineId())
                .orElseThrow(() -> new IllegalArgumentException("Airline not found with ID: " + createRequest.getAirlineId()));

        Aircraft aircraft = aircraftMapper.toEntity(createRequest);
        aircraft.setAirline(airline);
        Aircraft savedAircraft = aircraftRepository.save(aircraft);
        
        // Publish event to Kafka
        eventPublisherService.publishAircraftEvent(savedAircraft, EventType.CREATED);
        
        log.info("Aircraft created successfully with ID: {}", savedAircraft.getId());
        return aircraftMapper.toResponse(savedAircraft);
    }

    /**
     * ID ile aircraft getirir
     * 
     * @param id aircraft ID
     * @return aircraft response
     */
    @Transactional(readOnly = true)
    public AircraftDto.Response getAircraftById(Long id) {
        log.debug("Getting aircraft by ID: {}", id);
        
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aircraft not found with ID: " + id));
        
        return aircraftMapper.toResponse(aircraft);
    }

    /**
     * Registration ile aircraft getirir
     * 
     * @param registration registration
     * @return aircraft response
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "aircraft", key = "#registration")
    public AircraftDto.Response getAircraftByRegistration(String registration) {
        log.debug("Getting aircraft by registration: {}", registration);
        
        Aircraft aircraft = aircraftRepository.findByRegistration(registration)
                .orElseThrow(() -> new IllegalArgumentException("Aircraft not found with registration: " + registration));
        
        return aircraftMapper.toResponse(aircraft);
    }

    /**
     * Tüm aktif aircraft'ları listeler
     * 
     * @return aircraft listesi
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "aircraft", key = "'active'")
    public List<AircraftDto.ListResponse> getAllActiveAircraft() {
        log.debug("Getting all active aircraft");
        
        List<Aircraft> aircraft = aircraftRepository.findByActiveTrue();
        return aircraftMapper.toListResponseList(aircraft);
    }

    /**
     * Sayfalı olarak aktif aircraft'ları listeler
     * 
     * @param pageable sayfalama bilgisi
     * @return sayfalı aircraft listesi
     */
    @Transactional(readOnly = true)
    public Page<AircraftDto.ListResponse> getActiveAircraft(Pageable pageable) {
        log.debug("Getting active aircraft with pagination: {}", pageable);
        
        Page<Aircraft> aircraft = aircraftRepository.findByActiveTrue(pageable);
        return aircraft.map(aircraftMapper::toListResponse);
    }

    /**
     * Aircraft günceller
     * 
     * @param id aircraft ID
     * @param updateRequest update request
     * @return güncellenmiş aircraft response
     */
    @CacheEvict(value = "aircraft", allEntries = true)
    public AircraftDto.Response updateAircraft(Long id, AircraftDto.UpdateRequest updateRequest) {
        log.info("Updating aircraft with ID: {}", id);
        
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aircraft not found with ID: " + id));

        aircraftMapper.updateEntityFromUpdateRequest(updateRequest, aircraft);
        Aircraft updatedAircraft = aircraftRepository.save(aircraft);
        
        // Publish event to Kafka
        eventPublisherService.publishAircraftEvent(updatedAircraft, EventType.UPDATED);
        
        log.info("Aircraft updated successfully with ID: {}", updatedAircraft.getId());
        return aircraftMapper.toResponse(updatedAircraft);
    }

    /**
     * Aircraft'ı aktif/pasif yapar
     * 
     * @param id aircraft ID
     * @param active aktif durumu
     * @return güncellenmiş aircraft response
     */
    @CacheEvict(value = "aircraft", allEntries = true)
    public AircraftDto.Response updateAircraftStatus(Long id, boolean active) {
        log.info("Updating aircraft status with ID: {} to active: {}", id, active);
        
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aircraft not found with ID: " + id));

        if (active) {
            aircraft.activate();
        } else {
            aircraft.deactivate();
        }

        Aircraft updatedAircraft = aircraftRepository.save(aircraft);
        
        // Publish event to Kafka
        eventPublisherService.publishAircraftEvent(updatedAircraft, EventType.STATUS_CHANGED);
        
        log.info("Aircraft status updated successfully with ID: {}", updatedAircraft.getId());
        return aircraftMapper.toResponse(updatedAircraft);
    }

    /**
     * Aircraft siler (soft delete)
     * 
     * @param id aircraft ID
     */
    @CacheEvict(value = "aircraft", allEntries = true)
    public void deleteAircraft(Long id) {
        log.info("Deleting aircraft with ID: {}", id);
        
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aircraft not found with ID: " + id));

        aircraft.deactivate();
        Aircraft deletedAircraft = aircraftRepository.save(aircraft);
        
        // Publish event to Kafka
        eventPublisherService.publishAircraftEvent(deletedAircraft, EventType.DELETED);
        
        log.info("Aircraft deleted successfully with ID: {}", id);
    }

    /**
     * Airline ID ile aircraft'ları listeler
     * 
     * @param airlineId airline ID
     * @return aircraft listesi
     */
    @Transactional(readOnly = true)
    public List<AircraftDto.ListResponse> getAircraftByAirlineId(Long airlineId) {
        log.debug("Getting aircraft by airline ID: {}", airlineId);
        
        List<Aircraft> aircraft = aircraftRepository.findByAirlineIdAndActiveTrue(airlineId);
        return aircraftMapper.toListResponseList(aircraft);
    }

    /**
     * Airline code ile aircraft'ları listeler
     * 
     * @param airlineCode airline code
     * @return aircraft listesi
     */
    @Transactional(readOnly = true)
    public List<AircraftDto.ListResponse> getAircraftByAirlineCode(String airlineCode) {
        log.debug("Getting aircraft by airline code: {}", airlineCode);
        
        List<Aircraft> aircraft = aircraftRepository.findByAirlineCodeAndActiveTrue(airlineCode);
        return aircraftMapper.toListResponseList(aircraft);
    }

    /**
     * Aircraft type ile aircraft'ları listeler
     * 
     * @param aircraftType aircraft type
     * @return aircraft listesi
     */
    @Transactional(readOnly = true)
    public List<AircraftDto.ListResponse> getAircraftByType(String aircraftType) {
        log.debug("Getting aircraft by type: {}", aircraftType);
        
        List<Aircraft> aircraft = aircraftRepository.findByAircraftTypeAndActiveTrue(aircraftType);
        return aircraftMapper.toListResponseList(aircraft);
    }

    /**
     * Manufacturer ile aircraft'ları listeler
     * 
     * @param manufacturer manufacturer
     * @return aircraft listesi
     */
    @Transactional(readOnly = true)
    public List<AircraftDto.ListResponse> getAircraftByManufacturer(String manufacturer) {
        log.debug("Getting aircraft by manufacturer: {}", manufacturer);
        
        List<Aircraft> aircraft = aircraftRepository.findByManufacturerAndActiveTrue(manufacturer);
        return aircraftMapper.toListResponseList(aircraft);
    }

    /**
     * Registration ile arama yapar
     * 
     * @param registration registration
     * @return aircraft listesi
     */
    @Transactional(readOnly = true)
    public List<AircraftDto.ListResponse> searchAircraftByRegistration(String registration) {
        log.debug("Searching aircraft by registration: {}", registration);
        
        List<Aircraft> aircraft = aircraftRepository.findByRegistrationContainingIgnoreCaseAndActiveTrue(registration);
        return aircraftMapper.toListResponseList(aircraft);
    }

    /**
     * Aircraft type ile arama yapar
     * 
     * @param aircraftType aircraft type
     * @return aircraft listesi
     */
    @Transactional(readOnly = true)
    public List<AircraftDto.ListResponse> searchAircraftByType(String aircraftType) {
        log.debug("Searching aircraft by type: {}", aircraftType);
        
        List<Aircraft> aircraft = aircraftRepository.findByAircraftTypeContainingIgnoreCaseAndActiveTrue(aircraftType);
        return aircraftMapper.toListResponseList(aircraft);
    }

    /**
     * Manufacturer ile arama yapar
     * 
     * @param manufacturer manufacturer
     * @return aircraft listesi
     */
    @Transactional(readOnly = true)
    public List<AircraftDto.ListResponse> searchAircraftByManufacturer(String manufacturer) {
        log.debug("Searching aircraft by manufacturer: {}", manufacturer);
        
        List<Aircraft> aircraft = aircraftRepository.findByManufacturerContainingIgnoreCaseAndActiveTrue(manufacturer);
        return aircraftMapper.toListResponseList(aircraft);
    }

    /**
     * Capacity aralığında aircraft'ları arar
     * 
     * @param minCapacity minimum capacity
     * @param maxCapacity maximum capacity
     * @return aircraft listesi
     */
    @Transactional(readOnly = true)
    public List<AircraftDto.ListResponse> getAircraftByCapacityRange(Integer minCapacity, Integer maxCapacity) {
        log.debug("Getting aircraft by capacity range: {} - {}", minCapacity, maxCapacity);
        
        List<Aircraft> aircraft = aircraftRepository.findByCapacityBetweenAndActiveTrue(minCapacity, maxCapacity);
        return aircraftMapper.toListResponseList(aircraft);
    }
} 