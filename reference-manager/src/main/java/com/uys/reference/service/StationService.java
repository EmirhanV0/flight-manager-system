package com.uys.reference.service;

import com.uys.reference.dto.StationDto;
import com.uys.reference.entity.Station;
import com.uys.reference.event.enums.EventType;
import com.uys.reference.event.publisher.EventPublisherService;
import com.uys.reference.mapper.StationMapper;
import com.uys.reference.repository.StationRepository;
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
 * Station Service - Havaalanı iş mantığı katmanı
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StationService {

    private final StationRepository stationRepository;
    private final StationMapper stationMapper;
    private final EventPublisherService eventPublisherService;

    /**
     * Yeni station oluşturur
     * 
     * @param createRequest create request
     * @return oluşturulan station response
     */
    @CacheEvict(value = "stations", allEntries = true)
    public StationDto.Response createStation(StationDto.CreateRequest createRequest) {
        log.info("Creating station with code: {}", createRequest.getStationCode());

        // Station code'un benzersiz olduğunu kontrol et
        if (stationRepository.existsByStationCode(createRequest.getStationCode())) {
            throw new IllegalArgumentException("Station with code " + createRequest.getStationCode() + " already exists");
        }

        // Station name'in benzersiz olduğunu kontrol et
        if (stationRepository.existsByStationNameIgnoreCase(createRequest.getStationName())) {
            throw new IllegalArgumentException("Station with name " + createRequest.getStationName() + " already exists");
        }

        Station station = stationMapper.toEntity(createRequest);
        Station savedStation = stationRepository.save(station);
        
        // Publish event to Kafka
        eventPublisherService.publishStationEvent(savedStation, EventType.CREATED);
        
        log.info("Station created successfully with ID: {}", savedStation.getId());
        return stationMapper.toResponse(savedStation);
    }

    /**
     * ID ile station getirir
     * 
     * @param id station ID
     * @return station response
     */
    @Transactional(readOnly = true)
    public StationDto.Response getStationById(Long id) {
        log.debug("Getting station by ID: {}", id);
        
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Station not found with ID: " + id));
        
        return stationMapper.toResponse(station);
    }

    /**
     * Station code ile station getirir
     * 
     * @param stationCode station code
     * @return station response
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "stations", key = "#stationCode")
    public StationDto.Response getStationByCode(String stationCode) {
        log.debug("Getting station by code: {}", stationCode);
        
        Station station = stationRepository.findByStationCode(stationCode)
                .orElseThrow(() -> new IllegalArgumentException("Station not found with code: " + stationCode));
        
        return stationMapper.toResponse(station);
    }

    /**
     * Tüm aktif station'ları listeler
     * 
     * @return station listesi
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "stations", key = "'active'")
    public List<StationDto.ListResponse> getAllActiveStations() {
        log.debug("Getting all active stations");
        
        List<Station> stations = stationRepository.findByActiveTrue();
        return stationMapper.toListResponseList(stations);
    }

    /**
     * Sayfalı olarak aktif station'ları listeler
     * 
     * @param pageable sayfalama bilgisi
     * @return sayfalı station listesi
     */
    @Transactional(readOnly = true)
    public Page<StationDto.ListResponse> getActiveStations(Pageable pageable) {
        log.debug("Getting active stations with pagination: {}", pageable);
        
        Page<Station> stations = stationRepository.findByActiveTrue(pageable);
        return stations.map(stationMapper::toListResponse);
    }

    /**
     * Station günceller
     * 
     * @param id station ID
     * @param updateRequest update request
     * @return güncellenmiş station response
     */
    @CacheEvict(value = "stations", allEntries = true)
    public StationDto.Response updateStation(Long id, StationDto.UpdateRequest updateRequest) {
        log.info("Updating station with ID: {}", id);
        
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Station not found with ID: " + id));

        // Station name değişiyorsa benzersizlik kontrolü yap
        if (updateRequest.getStationName() != null && 
            !updateRequest.getStationName().equalsIgnoreCase(station.getStationName()) &&
            stationRepository.existsByStationNameIgnoreCase(updateRequest.getStationName())) {
            throw new IllegalArgumentException("Station with name " + updateRequest.getStationName() + " already exists");
        }

        stationMapper.updateEntityFromUpdateRequest(updateRequest, station);
        Station updatedStation = stationRepository.save(station);
        
        // Publish event to Kafka
        eventPublisherService.publishStationEvent(updatedStation, EventType.UPDATED);
        
        log.info("Station updated successfully with ID: {}", updatedStation.getId());
        return stationMapper.toResponse(updatedStation);
    }

    /**
     * Station'ı aktif/pasif yapar
     * 
     * @param id station ID
     * @param active aktif durumu
     * @return güncellenmiş station response
     */
    @CacheEvict(value = "stations", allEntries = true)
    public StationDto.Response updateStationStatus(Long id, boolean active) {
        log.info("Updating station status with ID: {} to active: {}", id, active);
        
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Station not found with ID: " + id));

        if (active) {
            station.activate();
        } else {
            station.deactivate();
        }

        Station updatedStation = stationRepository.save(station);
        
        // Publish event to Kafka
        eventPublisherService.publishStationEvent(updatedStation, EventType.STATUS_CHANGED);
        
        log.info("Station status updated successfully with ID: {}", updatedStation.getId());
        return stationMapper.toResponse(updatedStation);
    }

    /**
     * Station siler (soft delete)
     * 
     * @param id station ID
     */
    @CacheEvict(value = "stations", allEntries = true)
    public void deleteStation(Long id) {
        log.info("Deleting station with ID: {}", id);
        
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Station not found with ID: " + id));

        station.deactivate();
        Station deletedStation = stationRepository.save(station);
        
        // Publish event to Kafka
        eventPublisherService.publishStationEvent(deletedStation, EventType.DELETED);
        
        log.info("Station deleted successfully with ID: {}", id);
    }

    /**
     * Country ile station'ları listeler
     * 
     * @param country country
     * @return station listesi
     */
    @Transactional(readOnly = true)
    public List<StationDto.ListResponse> getStationsByCountry(String country) {
        log.debug("Getting stations by country: {}", country);
        
        List<Station> stations = stationRepository.findByCountryAndActiveTrue(country);
        return stationMapper.toListResponseList(stations);
    }

    /**
     * City ile station'ları listeler
     * 
     * @param city city
     * @return station listesi
     */
    @Transactional(readOnly = true)
    public List<StationDto.ListResponse> getStationsByCity(String city) {
        log.debug("Getting stations by city: {}", city);
        
        List<Station> stations = stationRepository.findByCityAndActiveTrue(city);
        return stationMapper.toListResponseList(stations);
    }

    /**
     * Timezone ile station'ları listeler
     * 
     * @param timezone timezone
     * @return station listesi
     */
    @Transactional(readOnly = true)
    public List<StationDto.ListResponse> getStationsByTimezone(String timezone) {
        log.debug("Getting stations by timezone: {}", timezone);
        
        List<Station> stations = stationRepository.findByTimezoneAndActiveTrue(timezone);
        return stationMapper.toListResponseList(stations);
    }

    /**
     * Station name ile arama yapar
     * 
     * @param stationName station name
     * @return station listesi
     */
    @Transactional(readOnly = true)
    public List<StationDto.ListResponse> searchStationsByName(String stationName) {
        log.debug("Searching stations by name: {}", stationName);
        
        List<Station> stations = stationRepository.findByStationNameContainingIgnoreCaseAndActiveTrue(stationName);
        return stationMapper.toListResponseList(stations);
    }

    /**
     * Station code ile arama yapar
     * 
     * @param stationCode station code
     * @return station listesi
     */
    @Transactional(readOnly = true)
    public List<StationDto.ListResponse> searchStationsByCode(String stationCode) {
        log.debug("Searching stations by code: {}", stationCode);
        
        List<Station> stations = stationRepository.findByStationCodeContainingIgnoreCaseAndActiveTrue(stationCode);
        return stationMapper.toListResponseList(stations);
    }

    /**
     * City ile arama yapar
     * 
     * @param city city
     * @return station listesi
     */
    @Transactional(readOnly = true)
    public List<StationDto.ListResponse> searchStationsByCity(String city) {
        log.debug("Searching stations by city: {}", city);
        
        List<Station> stations = stationRepository.findByCityContainingIgnoreCaseAndActiveTrue(city);
        return stationMapper.toListResponseList(stations);
    }

    /**
     * Country ile arama yapar
     * 
     * @param country country
     * @return station listesi
     */
    @Transactional(readOnly = true)
    public List<StationDto.ListResponse> searchStationsByCountry(String country) {
        log.debug("Searching stations by country: {}", country);
        
        List<Station> stations = stationRepository.findByCountryContainingIgnoreCaseAndActiveTrue(country);
        return stationMapper.toListResponseList(stations);
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
    @Transactional(readOnly = true)
    public List<StationDto.ListResponse> getStationsByLocationRange(Double minLat, Double maxLat, Double minLon, Double maxLon) {
        log.debug("Getting stations by location range: lat({}-{}), lon({}-{})", minLat, maxLat, minLon, maxLon);
        
        List<Station> stations = stationRepository.findByLocationBetweenAndActiveTrue(minLat, maxLat, minLon, maxLon);
        return stationMapper.toListResponseList(stations);
    }
} 