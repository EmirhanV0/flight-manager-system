package com.uys.flight.service;

import com.uys.flight.dto.FlightDto;
import com.uys.flight.entity.Flight;
import com.uys.flight.enums.FlightStatus;
import com.uys.flight.mapper.FlightMapper;
import com.uys.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Flight Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;

    @CacheEvict(value = "flights", allEntries = true)
    public FlightDto.Response createFlight(FlightDto.CreateRequest createRequest) {
        log.info("Creating flight: {}", createRequest.getFlightNumber());
        
        if (flightRepository.existsByFlightNumber(createRequest.getFlightNumber())) {
            throw new IllegalArgumentException("Flight number already exists: " + createRequest.getFlightNumber());
        }
        
        Flight flight = flightMapper.toEntity(createRequest);
        Flight savedFlight = flightRepository.save(flight);
        
        log.info("Flight created successfully: {}", savedFlight.getId());
        return flightMapper.toResponse(savedFlight);
    }

    @Cacheable(value = "flights", key = "#flightNumber")
    @Transactional(readOnly = true)
    public Optional<FlightDto.Response> getFlightByNumber(String flightNumber) {
        log.debug("Getting flight by number: {}", flightNumber);
        
        return flightRepository.findByFlightNumber(flightNumber)
                .map(flightMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public FlightDto.Response getFlightById(Long id) {
        log.debug("Getting flight by ID: {}", id);
        
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + id));
        
        return flightMapper.toResponse(flight);
    }

    @CacheEvict(value = "flights", allEntries = true)
    public FlightDto.Response updateFlight(Long id, FlightDto.UpdateRequest updateRequest) {
        log.info("Updating flight: {}", id);
        
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + id));
        
        flightMapper.updateEntityFromUpdateRequest(updateRequest, flight);
        Flight updatedFlight = flightRepository.save(flight);
        
        log.info("Flight updated successfully: {}", id);
        return flightMapper.toResponse(updatedFlight);
    }

    @CacheEvict(value = "flights", allEntries = true)
    public void deleteFlight(Long id) {
        log.info("Deleting flight: {}", id);
        
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + id));
        
        flight.setActive(false);
        flightRepository.save(flight);
        
        log.info("Flight deleted successfully: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<FlightDto.Response> getAllFlights(Pageable pageable) {
        log.debug("Getting all flights with pagination: {}", pageable);
        
        Page<Flight> flights = flightRepository.findByActiveTrue(pageable);
        return flights.map(flightMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<FlightDto.ListResponse> getFlightsByAirline(String airlineCode) {
        log.debug("Getting flights by airline: {}", airlineCode);
        
        List<Flight> flights = flightRepository.findByAirlineCode(airlineCode);
        return flightMapper.toListResponseList(flights);
    }

    @Transactional(readOnly = true)
    public List<FlightDto.ListResponse> getFlightsByRoute(String departure, String arrival) {
        log.debug("Getting flights by route: {} -> {}", departure, arrival);
        
        List<Flight> flights = flightRepository.findByRoute(departure, arrival);
        return flightMapper.toListResponseList(flights);
    }

    @Transactional(readOnly = true)
    public List<FlightDto.ListResponse> getFlightsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Getting flights by time range: {} -> {}", startTime, endTime);
        
        List<Flight> flights = flightRepository.findByDepartureTimeBetween(startTime, endTime);
        return flightMapper.toListResponseList(flights);
    }

    @CacheEvict(value = "flights", allEntries = true)
    public FlightDto.Response updateFlightStatus(Long id, FlightStatus status) {
        log.info("Updating flight status: {} to {}", id, status);
        
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + id));
        
        flight.setStatus(status);
        
        if (status == FlightStatus.DEPARTED && flight.getActualDepartureTime() == null) {
            flight.setActualDepartureTime(LocalDateTime.now());
        }
        
        if (status == FlightStatus.ARRIVED && flight.getActualArrivalTime() == null) {
            flight.setActualArrivalTime(LocalDateTime.now());
        }
        
        Flight updatedFlight = flightRepository.save(flight);
        
        log.info("Flight status updated successfully: {} to {}", id, status);
        return flightMapper.toResponse(updatedFlight);
    }

    @CacheEvict(value = "flights", allEntries = true)
    public FlightDto.Response delayFlight(Long id, int delayMinutes) {
        log.info("Delaying flight: {} by {} minutes", id, delayMinutes);
        
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + id));
        
        flight.delay(delayMinutes);
        Flight updatedFlight = flightRepository.save(flight);
        
        log.info("Flight delayed successfully: {} by {} minutes", id, delayMinutes);
        return flightMapper.toResponse(updatedFlight);
    }
}