package com.uys.reference.repository;

import com.uys.reference.entity.Airline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for AirlineRepository
 * Uses Testcontainers for real MySQL database testing
 */
@DataJpaTest
@ActiveProfiles("test")
class AirlineRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AirlineRepository airlineRepository;

    private Airline turkishAirlines;
    private Airline pegasus;
    private Airline lufthansa;

    @BeforeEach
    void setUp() {
        // Clear existing data
        airlineRepository.deleteAll();
        entityManager.flush();

        // Create test data
        turkishAirlines = Airline.builder()
                .airlineCode("TK")
                .airlineName("Turkish Airlines")
                .country("Turkey")
                .city("Istanbul")
                .description("National flag carrier of Turkey")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(0L)
                .build();

        pegasus = Airline.builder()
                .airlineCode("PC")
                .airlineName("Pegasus Airlines")
                .country("Turkey")
                .city("Istanbul")
                .description("Low-cost carrier")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(0L)
                .build();

        lufthansa = Airline.builder()
                .airlineCode("LH")
                .airlineName("Lufthansa")
                .country("Germany")
                .city("Frankfurt")
                .description("German flag carrier")
                .active(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(0L)
                .build();

        // Persist test data
        entityManager.persistAndFlush(turkishAirlines);
        entityManager.persistAndFlush(pegasus);
        entityManager.persistAndFlush(lufthansa);
    }

    @Test
    void findByAirlineCode_Success() {
        // When
        Optional<Airline> result = airlineRepository.findByAirlineCode("TK");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Turkish Airlines", result.get().getAirlineName());
        assertEquals("Turkey", result.get().getCountry());
    }

    @Test
    void findByAirlineCode_NotFound() {
        // When
        Optional<Airline> result = airlineRepository.findByAirlineCode("XX");

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void existsByAirlineCode_True() {
        // When
        boolean exists = airlineRepository.existsByAirlineCode("TK");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByAirlineCode_False() {
        // When
        boolean exists = airlineRepository.existsByAirlineCode("XX");

        // Then
        assertFalse(exists);
    }

    @Test
    void findByActiveTrue_ReturnsOnlyActiveAirlines() {
        // When
        List<Airline> result = airlineRepository.findByActiveTrue();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(Airline::getActive));
        assertTrue(result.stream().anyMatch(a -> a.getAirlineCode().equals("TK")));
        assertTrue(result.stream().anyMatch(a -> a.getAirlineCode().equals("PC")));
    }

    @Test
    void findByActiveFalse_ReturnsOnlyInactiveAirlines() {
        // When
        List<Airline> result = airlineRepository.findByActiveFalse();

        // Then
        assertEquals(1, result.size());
        assertFalse(result.get(0).getActive());
        assertEquals("LH", result.get(0).getAirlineCode());
    }

    @Test
    void findByCountry_Success() {
        // When
        List<Airline> result = airlineRepository.findByCountry("Turkey");

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(a -> a.getCountry().equals("Turkey")));
    }

    @Test
    void findByCountryAndActiveTrue_Success() {
        // When
        List<Airline> result = airlineRepository.findByCountryAndActiveTrue("Turkey");

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(a -> "Turkey".equals(a.getCountry()) && Boolean.TRUE.equals(a.getActive())));
    }

    @Test
    void findByAirlineNameContainingIgnoreCase_Success() {
        // When
        List<Airline> result = airlineRepository.findByAirlineNameContainingIgnoreCase("turkish");

        // Then
        assertEquals(1, result.size());
        assertEquals("TK", result.get(0).getAirlineCode());
    }

    @Test
    void findByAirlineNameContainingIgnoreCaseOrCountryContainingIgnoreCase_Success() {
        // When - Search by name
        List<Airline> result1 = airlineRepository
                .findByAirlineNameContainingIgnoreCaseOrCountryContainingIgnoreCase("pegasus", "pegasus");

        // Then
        assertEquals(1, result1.size());
        assertEquals("PC", result1.get(0).getAirlineCode());

        // When - Search by country
        List<Airline> result2 = airlineRepository
                .findByAirlineNameContainingIgnoreCaseOrCountryContainingIgnoreCase("germany", "germany");

        // Then
        assertEquals(1, result2.size());
        assertEquals("LH", result2.get(0).getAirlineCode());
    }

    @Test
    void findActiveAirlinesWithPagination_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 1);

        // When
        Page<Airline> result = airlineRepository.findByActiveTrue(pageable);

        // Then
        assertEquals(1, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertTrue(result.getContent().get(0).getActive());
    }

    @Test
    void countByActiveTrue_Success() {
        // When
        long count = airlineRepository.countByActiveTrue();

        // Then
        assertEquals(2, count);
    }

    @Test
    void countByActiveFalse_Success() {
        // When
        long count = airlineRepository.countByActiveFalse();

        // Then
        assertEquals(1, count);
    }

    @Test
    void findByCity_Success() {
        // When
        List<Airline> result = airlineRepository.findByCity("Istanbul");

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(a -> a.getCity().equals("Istanbul")));
    }

    @Test
    void customQuery_FindAirlinesByCountryOrderByName_Success() {
        // When
        List<Airline> result = airlineRepository.findByCountryOrderByAirlineNameAsc("Turkey");

        // Then
        assertEquals(2, result.size());
        assertEquals("PC", result.get(0).getAirlineCode()); // Pegasus comes before Turkish alphabetically
        assertEquals("TK", result.get(1).getAirlineCode());
    }

    @Test
    void saveAndUpdate_Success() {
        // Given
        Airline newAirline = Airline.builder()
                .airlineCode("AA")
                .airlineName("American Airlines")
                .country("USA")
                .city("Dallas")
                .description("Major US carrier")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(0L)
                .build();

        // When - Save
        Airline saved = airlineRepository.save(newAirline);

        // Then
        assertNotNull(saved.getId());
        assertEquals("AA", saved.getAirlineCode());

        // When - Update
        saved.setDescription("Updated description");
        Airline updated = airlineRepository.save(saved);

        // Then
        assertEquals("Updated description", updated.getDescription());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void deleteByAirlineCode_Success() {
        // Given
        assertTrue(airlineRepository.existsByAirlineCode("TK"));

        // When
        Optional<Airline> airline = airlineRepository.findByAirlineCode("TK");
        assertTrue(airline.isPresent());
        airlineRepository.delete(airline.get());

        // Then
        assertFalse(airlineRepository.existsByAirlineCode("TK"));
    }
}