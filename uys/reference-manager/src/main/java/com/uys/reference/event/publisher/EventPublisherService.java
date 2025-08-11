package com.uys.reference.event.publisher;

import com.uys.reference.entity.Airline;
import com.uys.reference.entity.Aircraft;
import com.uys.reference.entity.Station;
import com.uys.reference.event.enums.EventType;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Event Publisher Service (DISABLED FOR DEVELOPMENT)
 * 
 * Bu servis geliştirme aşamasında devre dışı bırakılmıştır.
 * Kafka dependency'leri kaldırıldığı için bu servis şu anda kullanılmamaktadır.
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@Service
@Slf4j
public class EventPublisherService {

    // Kafka Topics (DISABLED FOR DEVELOPMENT)
    // private static final String AIRLINE_EVENTS_TOPIC = "airline-events";
    // private static final String AIRCRAFT_EVENTS_TOPIC = "aircraft-events";
    // private static final String STATION_EVENTS_TOPIC = "station-events";
    
    /**
     * Publish airline event to Kafka (DISABLED FOR DEVELOPMENT)
     */
    public void publishAirlineEvent(Airline airline, EventType eventType) {
        try {
            // AirlineEventDto eventDto = AirlineEventDto.builder()
            //         .eventId(UUID.randomUUID().toString())
            //         .eventType(eventType.name())
            //         .airlineCode(airline.getAirlineCode())
            //         .airlineName(airline.getAirlineName())
            //         .country(airline.getCountry())
            //         .city(airline.getCity())
            //         .description(airline.getDescription())
            //         .active(airline.getActive())
            //         .eventTimestamp(LocalDateTime.now())
            //         .createdAt(airline.getCreatedAt())
            //         .updatedAt(airline.getUpdatedAt())
            //         .build();
            
            // String eventJson = objectMapper.writeValueAsString(eventDto);
            // kafkaTemplate.send(AIRLINE_EVENTS_TOPIC, airline.getAirlineCode(), eventJson);
            log.info("Airline event prepared (Kafka disabled): {} for airline: {}", eventType, airline.getAirlineCode());
            
        } catch (Exception e) {
            log.error("Failed to prepare airline event for airline: {}", airline.getAirlineCode(), e);
        }
    }
    
    /**
     * Publish aircraft event to Kafka (DISABLED FOR DEVELOPMENT)
     */
    public void publishAircraftEvent(Aircraft aircraft, EventType eventType) {
        try {
            // AircraftEventDto eventDto = AircraftEventDto.builder()
            //         .eventId(UUID.randomUUID().toString())
            //         .eventType(eventType.name())
            //         .registration(aircraft.getRegistration())
            //         .aircraftType(aircraft.getAircraftType())
            //         .model(aircraft.getModel())
            //         .manufacturer(aircraft.getManufacturer())
            //         .capacity(aircraft.getCapacity())
            //         .maxRange(aircraft.getMaxRange())
            //         .cruiseSpeed(aircraft.getCruiseSpeed())
            //         .airlineCode(aircraft.getAirline() != null ? aircraft.getAirline().getAirlineCode() : null)
            //         .airlineName(aircraft.getAirline() != null ? aircraft.getAirline().getAirlineName() : null)
            //         .active(aircraft.getActive())
            //         .eventTimestamp(LocalDateTime.now())
            //         .createdAt(aircraft.getCreatedAt())
            //         .updatedAt(aircraft.getUpdatedAt())
            //         .build();
            
            // String eventJson = objectMapper.writeValueAsString(eventDto);
            // kafkaTemplate.send(AIRCRAFT_EVENTS_TOPIC, aircraft.getRegistration(), eventJson);
            log.info("Aircraft event prepared (Kafka disabled): {} for aircraft: {}", eventType, aircraft.getRegistration());
            
        } catch (Exception e) {
            log.error("Failed to prepare aircraft event for aircraft: {}", aircraft.getRegistration(), e);
        }
    }
    
    /**
     * Publish station event to Kafka (DISABLED FOR DEVELOPMENT)
     */
    public void publishStationEvent(Station station, EventType eventType) {
        try {
            // StationEventDto eventDto = StationEventDto.builder()
            //         .eventId(UUID.randomUUID().toString())
            //         .eventType(eventType.name())
            //         .stationCode(station.getStationCode())
            //         .stationName(station.getStationName())
            //         .city(station.getCity())
            //         .country(station.getCountry())
            //         .address(station.getAddress())
            //         .timezone(station.getTimezone())
            //         .latitude(station.getLatitude())
            //         .longitude(station.getLongitude())
            //         .altitude(station.getAltitude())
            //         .description(station.getDescription())
            //         .active(station.getActive())
            //         .eventTimestamp(LocalDateTime.now())
            //         .createdAt(station.getCreatedAt())
            //         .updatedAt(station.getUpdatedAt())
            //         .build();
            
            // String eventJson = objectMapper.writeValueAsString(eventDto);
            // kafkaTemplate.send(STATION_EVENTS_TOPIC, station.getStationCode(), eventJson);
            log.info("Station event prepared (Kafka disabled): {} for station: {}", eventType, station.getStationCode());
            
        } catch (Exception e) {
            log.error("Failed to prepare station event for station: {}", station.getStationCode(), e);
        }
    }
} 