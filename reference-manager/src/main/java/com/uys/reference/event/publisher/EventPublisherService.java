package com.uys.reference.event.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uys.reference.entity.Airline;
import com.uys.reference.entity.Aircraft;
import com.uys.reference.entity.Station;
import com.uys.reference.event.dto.AirlineEventDto;
import com.uys.reference.event.dto.AircraftEventDto;
import com.uys.reference.event.dto.StationEventDto;
import com.uys.reference.event.enums.EventType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisherService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    // Kafka Topics
    private static final String AIRLINE_EVENTS_TOPIC = "airline-events";
    private static final String AIRCRAFT_EVENTS_TOPIC = "aircraft-events";
    private static final String STATION_EVENTS_TOPIC = "station-events";
    
    /**
     * Publish airline event to Kafka
     */
    public void publishAirlineEvent(Airline airline, EventType eventType) {
        try {
            AirlineEventDto eventDto = AirlineEventDto.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType(eventType.name())
                    .airlineCode(airline.getAirlineCode())
                    .airlineName(airline.getAirlineName())
                    .country(airline.getCountry())
                    .city(airline.getCity())
                    .description(airline.getDescription())
                    .active(airline.getActive())
                    .eventTimestamp(LocalDateTime.now())
                    .createdAt(airline.getCreatedAt())
                    .updatedAt(airline.getUpdatedAt())
                    .build();
            
                    String eventJson = objectMapper.writeValueAsString(eventDto);
        kafkaTemplate.send(AIRLINE_EVENTS_TOPIC, airline.getAirlineCode(), eventJson);
            log.info("Published airline event: {} for airline: {}", eventType, airline.getAirlineCode());
            
        } catch (Exception e) {
            log.error("Failed to publish airline event for airline: {}", airline.getAirlineCode(), e);
            // In production, consider using a dead letter queue or retry mechanism
        }
    }
    
    /**
     * Publish aircraft event to Kafka
     */
    public void publishAircraftEvent(Aircraft aircraft, EventType eventType) {
        try {
            AircraftEventDto eventDto = AircraftEventDto.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType(eventType.name())
                    .registration(aircraft.getRegistration())
                    .aircraftType(aircraft.getAircraftType())
                    .model(aircraft.getModel())
                    .manufacturer(aircraft.getManufacturer())
                    .capacity(aircraft.getCapacity())
                    .maxRange(aircraft.getMaxRange())
                    .cruiseSpeed(aircraft.getCruiseSpeed())
                    .airlineCode(aircraft.getAirline() != null ? aircraft.getAirline().getAirlineCode() : null)
                    .airlineName(aircraft.getAirline() != null ? aircraft.getAirline().getAirlineName() : null)
                    .active(aircraft.getActive())
                    .eventTimestamp(LocalDateTime.now())
                    .createdAt(aircraft.getCreatedAt())
                    .updatedAt(aircraft.getUpdatedAt())
                    .build();
            
                    String eventJson = objectMapper.writeValueAsString(eventDto);
        kafkaTemplate.send(AIRCRAFT_EVENTS_TOPIC, aircraft.getRegistration(), eventJson);
            log.info("Published aircraft event: {} for aircraft: {}", eventType, aircraft.getRegistration());
            
        } catch (Exception e) {
            log.error("Failed to publish aircraft event for aircraft: {}", aircraft.getRegistration(), e);
        }
    }
    
    /**
     * Publish station event to Kafka
     */
    public void publishStationEvent(Station station, EventType eventType) {
        try {
            StationEventDto eventDto = StationEventDto.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType(eventType.name())
                    .stationCode(station.getStationCode())
                    .stationName(station.getStationName())
                    .city(station.getCity())
                    .country(station.getCountry())
                    .address(station.getAddress())
                    .timezone(station.getTimezone())
                    .latitude(station.getLatitude())
                    .longitude(station.getLongitude())
                    .altitude(station.getAltitude())
                    .description(station.getDescription())
                    .active(station.getActive())
                    .eventTimestamp(LocalDateTime.now())
                    .createdAt(station.getCreatedAt())
                    .updatedAt(station.getUpdatedAt())
                    .build();
            
                    String eventJson = objectMapper.writeValueAsString(eventDto);
        kafkaTemplate.send(STATION_EVENTS_TOPIC, station.getStationCode(), eventJson);
            log.info("Published station event: {} for station: {}", eventType, station.getStationCode());
            
        } catch (Exception e) {
            log.error("Failed to publish station event for station: {}", station.getStationCode(), e);
        }
    }
} 