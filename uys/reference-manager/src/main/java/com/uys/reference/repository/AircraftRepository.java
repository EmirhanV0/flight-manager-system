package com.uys.reference.repository;

import com.uys.reference.entity.Aircraft;
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
 * Aircraft Repository - Uçak veri erişim katmanı
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    /**
     * Registration ile aircraft arar
     * 
     * @param registration registration
     * @return optional aircraft
     */
    @Cacheable(value = "aircraft", key = "#registration")
    Optional<Aircraft> findByRegistration(String registration);

    /**
     * Aktif aircraft'ları listeler
     * 
     * @return aktif aircraft listesi
     */
    @Cacheable(value = "aircraft", key = "'active'")
    List<Aircraft> findByActiveTrue();

    /**
     * Aktif aircraft'ları sayfalı olarak listeler
     * 
     * @param pageable sayfalama bilgisi
     * @return sayfalı aktif aircraft listesi
     */
    Page<Aircraft> findByActiveTrue(Pageable pageable);

    /**
     * Airline ID ile aircraft'ları listeler
     * 
     * @param airlineId airline ID
     * @return aircraft listesi
     */
    List<Aircraft> findByAirlineIdAndActiveTrue(Long airlineId);

    /**
     * Aircraft type ile aircraft'ları listeler
     * 
     * @param aircraftType aircraft type
     * @return aircraft listesi
     */
    List<Aircraft> findByAircraftTypeAndActiveTrue(String aircraftType);

    /**
     * Manufacturer ile aircraft'ları listeler
     * 
     * @param manufacturer manufacturer
     * @return aircraft listesi
     */
    List<Aircraft> findByManufacturerAndActiveTrue(String manufacturer);

    /**
     * Registration ile aircraft'ın var olup olmadığını kontrol eder
     * 
     * @param registration registration
     * @return true if exists, false otherwise
     */
    boolean existsByRegistration(String registration);

    /**
     * Registration ile benzer aircraft'ları arar
     * 
     * @param registration registration
     * @return aircraft listesi
     */
    @Query("SELECT a FROM Aircraft a WHERE LOWER(a.registration) LIKE LOWER(CONCAT('%', :registration, '%')) AND a.active = true")
    List<Aircraft> findByRegistrationContainingIgnoreCaseAndActiveTrue(@Param("registration") String registration);

    /**
     * Aircraft type ile benzer aircraft'ları arar
     * 
     * @param aircraftType aircraft type
     * @return aircraft listesi
     */
    @Query("SELECT a FROM Aircraft a WHERE LOWER(a.aircraftType) LIKE LOWER(CONCAT('%', :aircraftType, '%')) AND a.active = true")
    List<Aircraft> findByAircraftTypeContainingIgnoreCaseAndActiveTrue(@Param("aircraftType") String aircraftType);

    /**
     * Manufacturer ile benzer aircraft'ları arar
     * 
     * @param manufacturer manufacturer
     * @return aircraft listesi
     */
    @Query("SELECT a FROM Aircraft a WHERE LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :manufacturer, '%')) AND a.active = true")
    List<Aircraft> findByManufacturerContainingIgnoreCaseAndActiveTrue(@Param("manufacturer") String manufacturer);

    /**
     * Capacity aralığında aircraft'ları arar
     * 
     * @param minCapacity minimum capacity
     * @param maxCapacity maximum capacity
     * @return aircraft listesi
     */
    @Query("SELECT a FROM Aircraft a WHERE a.capacity BETWEEN :minCapacity AND :maxCapacity AND a.active = true")
    List<Aircraft> findByCapacityBetweenAndActiveTrue(@Param("minCapacity") Integer minCapacity, @Param("maxCapacity") Integer maxCapacity);

    /**
     * Airline code ile aircraft'ları listeler
     * 
     * @param airlineCode airline code
     * @return aircraft listesi
     */
    @Query("SELECT a FROM Aircraft a JOIN a.airline al WHERE al.airlineCode = :airlineCode AND a.active = true")
    List<Aircraft> findByAirlineCodeAndActiveTrue(@Param("airlineCode") String airlineCode);
} 