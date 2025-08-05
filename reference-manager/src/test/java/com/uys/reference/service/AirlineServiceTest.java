package com.uys.reference.service;

import com.uys.reference.dto.AirlineDto;
import com.uys.reference.entity.Airline;
import com.uys.reference.event.enums.EventType;
import com.uys.reference.event.publisher.EventPublisherService;
import com.uys.reference.mapper.AirlineMapper;
import com.uys.reference.repository.AirlineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AirlineService
 */
@ExtendWith(MockitoExtension.class)
class AirlineServiceTest {

    @Mock
    private AirlineRepository airlineRepository;

    @Mock
    private AirlineMapper airlineMapper;

    @Mock
    private EventPublisherService eventPublisherService;

    @InjectMocks
    private AirlineService airlineService;

    private Airline testAirline;
    private AirlineDto.CreateRequest createRequest;
    private AirlineDto.UpdateRequest updateRequest;
    private AirlineDto.Response response;

    @BeforeEach
    void setUp() {
        // Test data setup
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
                .version(0L)
                .build();

        createRequest = AirlineDto.CreateRequest.builder()
                .airlineCode("TK")
                .airlineName("Turkish Airlines")
                .country("Turkey")
                .city("Istanbul")
                .description("National flag carrier")
                .build();

        updateRequest = AirlineDto.UpdateRequest.builder()
                .airlineName("Turkish Airlines Updated")
                .country("Turkey")
                .city("Istanbul")
                .description("Updated description")
                .build();

        response = AirlineDto.Response.builder()
                .id(1L)
                .airlineCode("TK")
                .airlineName("Turkish Airlines")
                .country("Turkey")
                .city("Istanbul")
                .description("National flag carrier")
                .active(true)
                .createdAt(testAirline.getCreatedAt())
                .updatedAt(testAirline.getUpdatedAt())
                .version(0L)
                .build();
    }

    @Test
    void createAirline_Success() {
        // Given
        when(airlineRepository.existsByAirlineCode("TK")).thenReturn(false);
        when(airlineMapper.toEntity(createRequest)).thenReturn(testAirline);
        when(airlineRepository.save(any(Airline.class))).thenReturn(testAirline);
        when(airlineMapper.toResponse(testAirline)).thenReturn(response);

        // When
        AirlineDto.Response result = airlineService.createAirline(createRequest);

        // Then
        assertNotNull(result);
        assertEquals("TK", result.getAirlineCode());
        assertEquals("Turkish Airlines", result.getAirlineName());
        verify(airlineRepository).save(any(Airline.class));
        verify(eventPublisherService).publishAirlineEvent(testAirline, EventType.CREATED);
    }

    @Test
    void createAirline_DuplicateCode_ThrowsException() {
        // Given
        when(airlineRepository.existsByAirlineCode("TK")).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            airlineService.createAirline(createRequest));
        verify(airlineRepository, never()).save(any());
        verify(eventPublisherService, never()).publishAirlineEvent(any(), any());
    }

    @Test
    void getAirlineByCode_Success() {
        // Given
        when(airlineRepository.findByAirlineCode("TK")).thenReturn(Optional.of(testAirline));
        when(airlineMapper.toResponse(testAirline)).thenReturn(response);

        // When
        AirlineDto.Response result = airlineService.getAirlineByCode("TK");

        // Then
        assertNotNull(result);
        assertEquals("TK", result.getAirlineCode());
        verify(airlineRepository).findByAirlineCode("TK");
    }

    @Test
    void getAirlineByCode_NotFound() {
        // Given
        when(airlineRepository.findByAirlineCode("XX")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            airlineService.getAirlineByCode("XX");
        });
    }

    @Test
    void updateAirline_Success() {
        // Given
        when(airlineRepository.findByAirlineCode("TK")).thenReturn(Optional.of(testAirline));
        when(airlineRepository.save(any(Airline.class))).thenReturn(testAirline);
        when(airlineMapper.toResponse(testAirline)).thenReturn(response);

        // When
        AirlineDto.Response result = airlineService.updateAirline("TK", updateRequest);

        // Then
        assertNotNull(result);
        verify(airlineMapper).updateEntityFromUpdateRequest(updateRequest, testAirline);
        verify(airlineRepository).save(testAirline);
        verify(eventPublisherService).publishAirlineEvent(testAirline, EventType.UPDATED);
    }

    @Test
    void updateAirline_NotFound_ThrowsException() {
        // Given
        when(airlineRepository.findByAirlineCode("XX")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
            airlineService.updateAirline("XX", updateRequest));
        verify(airlineRepository, never()).save(any());
        verify(eventPublisherService, never()).publishAirlineEvent(any(), any());
    }

    @Test
    void deleteAirline_Success() {
        // Given
        when(airlineRepository.findByAirlineCode("TK")).thenReturn(Optional.of(testAirline));

        // When
        airlineService.deleteAirline("TK");

        // Then
        verify(airlineRepository).delete(testAirline);
        verify(eventPublisherService).publishAirlineEvent(testAirline, EventType.DELETED);
    }

    @Test
    void changeAirlineStatus_Success() {
        // Given
        when(airlineRepository.findByAirlineCode("TK")).thenReturn(Optional.of(testAirline));
        when(airlineRepository.save(any(Airline.class))).thenReturn(testAirline);
        when(airlineMapper.toResponse(testAirline)).thenReturn(response);

        // When
        AirlineDto.Response result = airlineService.changeAirlineStatus("TK", false);

        // Then
        assertNotNull(result);
        verify(airlineRepository).save(testAirline);
        verify(eventPublisherService).publishAirlineEvent(testAirline, EventType.STATUS_CHANGED);
    }

    @Test
    void getAllAirlines_Success() {
        // Given
        List<Airline> airlines = Arrays.asList(testAirline);
        Page<Airline> airlinePage = new PageImpl<>(airlines);
        Pageable pageable = PageRequest.of(0, 10);

        when(airlineRepository.findAll(pageable)).thenReturn(airlinePage);
        when(airlineMapper.toResponseList(airlines)).thenReturn(Arrays.asList(response));

        // When
        Page<AirlineDto.Response> result = airlineService.getAllAirlines(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("TK", result.getContent().get(0).getAirlineCode());
    }

    @Test
    void getActiveAirlines_Success() {
        // Given
        List<Airline> airlines = Arrays.asList(testAirline);
        when(airlineRepository.findByActiveTrue()).thenReturn(airlines);
        when(airlineMapper.toListResponseList(airlines)).thenReturn(
            Arrays.asList(AirlineDto.ListResponse.builder()
                .airlineCode("TK")
                .airlineName("Turkish Airlines")
                .country("Turkey")
                .active(true)
                .build())
        );

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<AirlineDto.ListResponse> result = airlineService.getActiveAirlines(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("TK", result.getContent().get(0).getAirlineCode());
        assertTrue(result.getContent().get(0).getActive());
    }

    @Test
    void searchAirlines_Success() {
        // Given
        List<Airline> airlines = Arrays.asList(testAirline);
        when(airlineRepository.findByAirlineNameContainingIgnoreCaseOrCountryContainingIgnoreCase("Turkish", "Turkish"))
                .thenReturn(airlines);
        when(airlineMapper.toListResponseList(airlines)).thenReturn(
            Arrays.asList(AirlineDto.ListResponse.builder()
                .airlineCode("TK")
                .airlineName("Turkish Airlines")
                .country("Turkey")
                .active(true)
                .build())
        );

        // When
        List<AirlineDto.ListResponse> result = airlineService.searchAirlines("Turkish");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TK", result.get(0).getAirlineCode());
    }
}