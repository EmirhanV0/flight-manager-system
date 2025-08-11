package com.uys.flight.repository;

import com.uys.flight.entity.Flight;
import com.uys.flight.enums.FlightStatus;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Flight Repository
 */
@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Cacheable(value = "flights", key = "#flightNumber")
    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findByAirlineCode(String airlineCode);

    List<Flight> findByAircraftRegistration(String aircraftRegistration);

    List<Flight> findByDepartureStationCode(String departureStationCode);

    List<Flight> findByArrivalStationCode(String arrivalStationCode);

    List<Flight> findByStatus(FlightStatus status);

    Page<Flight> findByActiveTrue(Pageable pageable);

    @Query("SELECT f FROM Flight f WHERE f.scheduledDepartureTime BETWEEN :startTime AND :endTime")
    List<Flight> findByDepartureTimeBetween(@Param("startTime") LocalDateTime startTime, 
                                          @Param("endTime") LocalDateTime endTime);

    @Query("SELECT f FROM Flight f WHERE f.departureStationCode = :departure AND f.arrivalStationCode = :arrival")
    List<Flight> findByRoute(@Param("departure") String departure, @Param("arrival") String arrival);

    boolean existsByFlightNumber(String flightNumber);

    long countByAirlineCode(String airlineCode);

    long countByStatus(FlightStatus status);
}