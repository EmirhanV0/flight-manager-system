package com.uys.reference.repository;

import com.uys.reference.entity.Airline;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Airline Repository - Havayolu şirketi veri erişim katmanı
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {

    /**
     * Airline code ile airline arar
     * 
     * @param airlineCode airline code
     * @return optional airline
     */
    @Cacheable(value = "airlines", key = "#airlineCode")
    Optional<Airline> findByAirlineCode(String airlineCode);

    /**
     * Aktif airline'ları listeler
     * 
     * @return aktif airline listesi
     */
    @Cacheable(value = "airlines", key = "'active'")
    List<Airline> findByActiveTrue();

    /**
     * Aktif airline'ları sayfalı olarak listeler
     * 
     * @param pageable sayfalama bilgisi
     * @return sayfalı aktif airline listesi
     */
    Page<Airline> findByActiveTrue(Pageable pageable);

    /**
     * Airline name ile airline arar (case insensitive)
     * 
     * @param airlineName airline name
     * @return optional airline
     */
    Optional<Airline> findByAirlineNameIgnoreCase(String airlineName);

    /**
     * Airline code ile airline'ın var olup olmadığını kontrol eder
     * 
     * @param airlineCode airline code
     * @return true if exists, false otherwise
     */
    boolean existsByAirlineCode(String airlineCode);

    /**
     * Airline name ile airline'ın var olup olmadığını kontrol eder (case insensitive)
     * 
     * @param airlineName airline name
     * @return true if exists, false otherwise
     */
    boolean existsByAirlineNameIgnoreCase(String airlineName);

    /**
     * Country ile airline'ları listeler
     * 
     * @param country country
     * @return airline listesi
     */
    List<Airline> findByCountryAndActiveTrue(String country);

    /**
     * City ile airline'ları listeler
     * 
     * @param city city
     * @return airline listesi
     */
    List<Airline> findByCityAndActiveTrue(String city);

    /**
     * Airline name ile benzer airline'ları arar
     * 
     * @param airlineName airline name
     * @return airline listesi
     */
    @Query("SELECT a FROM Airline a WHERE LOWER(a.airlineName) LIKE LOWER(CONCAT('%', :airlineName, '%')) AND a.active = true")
    List<Airline> findByAirlineNameContainingIgnoreCaseAndActiveTrue(@Param("airlineName") String airlineName);

    /**
     * Airline code ile benzer airline'ları arar
     * 
     * @param airlineCode airline code
     * @return airline listesi
     */
    @Query("SELECT a FROM Airline a WHERE LOWER(a.airlineCode) LIKE LOWER(CONCAT('%', :airlineCode, '%')) AND a.active = true")
    List<Airline> findByAirlineCodeContainingIgnoreCaseAndActiveTrue(@Param("airlineCode") String airlineCode);

    // Additional methods for tests
    List<Airline> findByActiveFalse();
    List<Airline> findByCountry(String country);
    List<Airline> findByCity(String city);
    List<Airline> findByAirlineNameContainingIgnoreCase(String name);
    List<Airline> findByAirlineNameContainingIgnoreCaseOrCountryContainingIgnoreCase(String name, String country);
    List<Airline> findByCountryOrderByAirlineNameAsc(String country);
    long countByActiveTrue();
    long countByActiveFalse();
} 