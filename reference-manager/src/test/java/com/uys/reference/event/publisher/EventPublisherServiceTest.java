package com.uys.reference.event.publisher;

import com.uys.reference.entity.Airline;
import com.uys.reference.entity.Aircraft;
import com.uys.reference.entity.Station;
import com.uys.reference.event.enums.EventType;
import com.uys.reference.mapper.AirlineMapper;
import com.uys.reference.mapper.AircraftMapper;
import com.uys.reference.mapper.StationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class EventPublisherServiceTest {

    @Mock
    private AirlineMapper airlineMapper;

    @Mock
    private AircraftMapper aircraftMapper;

    @Mock
    private StationMapper stationMapper;

    @InjectMocks
    private EventPublisherService eventPublisherService;

    private Airline testAirline;
    private Aircraft testAircraft;
    private Station testStation;

    @BeforeEach
    void setUp() {
        // Setup test data
        testAirline = Airline.builder()
                .id(1L)
                .airlineCode("TK")
                .airlineName("Turkish Airlines")
                .country("Turkey")
                .city("Istanbul")
                .description("National flag carrier")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testAircraft = Aircraft.builder()
                .id(1L)
                .registration("TC-JRE")
                .aircraftType("A320")
                .model("A320-200")
                .manufacturer("Airbus")
                .capacity(180)
                .maxRange(6100)
                .cruiseSpeed(828)
                .airline(testAirline)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testStation = Station.builder()
                .id(1L)
                .stationCode("IST")
                .stationName("Istanbul Airport")
                .city("Istanbul")
                .country("Turkey")
                .address("Tayakadın, Terminal Caddesi No:1")
                .timezone("Europe/Istanbul")
                .latitude(41.2622)
                .longitude(28.7278)
                .altitude(99)
                .description("Main international airport")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void publishAirlineEvent_Created_Success() {
        // When & Then - Should not throw exception
        eventPublisherService.publishAirlineEvent(testAirline, EventType.CREATED);
    }

    @Test
    void publishAirlineEvent_Updated_Success() {
        // When & Then - Should not throw exception
        eventPublisherService.publishAirlineEvent(testAirline, EventType.UPDATED);
    }

    @Test
    void publishAirlineEvent_Deleted_Success() {
        // When & Then - Should not throw exception
        eventPublisherService.publishAirlineEvent(testAirline, EventType.DELETED);
    }

    @Test
    void publishAirlineEvent_StatusChanged_Success() {
        // When & Then - Should not throw exception
        eventPublisherService.publishAirlineEvent(testAirline, EventType.STATUS_CHANGED);
    }

    @Test
    void publishAircraftEvent_Created_Success() {
        // When & Then - Should not throw exception
        eventPublisherService.publishAircraftEvent(testAircraft, EventType.CREATED);
    }

    @Test
    void publishAircraftEvent_Updated_Success() {
        // When & Then - Should not throw exception
        eventPublisherService.publishAircraftEvent(testAircraft, EventType.UPDATED);
    }

    @Test
    void publishAircraftEvent_Deleted_Success() {
        // When & Then - Should not throw exception
        eventPublisherService.publishAircraftEvent(testAircraft, EventType.DELETED);
    }

    @Test
    void publishAircraftEvent_StatusChanged_Success() {
        // When & Then - Should not throw exception
        eventPublisherService.publishAircraftEvent(testAircraft, EventType.STATUS_CHANGED);
    }

    @Test
    void publishStationEvent_Created_Success() {
        // When & Then - Should not throw exception
        eventPublisherService.publishStationEvent(testStation, EventType.CREATED);
    }

    @Test
    void publishStationEvent_Updated_Success() {
        // When & Then - Should not throw exception
        eventPublisherService.publishStationEvent(testStation, EventType.UPDATED);
    }

    @Test
    void publishStationEvent_Deleted_Success() {
        // When & Then - Should not throw exception
        eventPublisherService.publishStationEvent(testStation, EventType.DELETED);
    }

    @Test
    void publishStationEvent_StatusChanged_Success() {
        // When & Then - Should not throw exception
        eventPublisherService.publishStationEvent(testStation, EventType.STATUS_CHANGED);
    }
} 