package com.uys.reference.repository;

import com.uys.reference.entity.Station;
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
 * Station Repository - Havaalanı veri erişim katmanı
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    /**
     * Station code ile station arar
     * 
     * @param stationCode station code
     * @return optional station
     */
    @Cacheable(value = "stations", key = "#stationCode")
    Optional<Station> findByStationCode(String stationCode);

    /**
     * Aktif station'ları listeler
     * 
     * @return aktif station listesi
     */
    @Cacheable(value = "stations", key = "'active'")
    List<Station> findByActiveTrue();

    /**
     * Aktif station'ları sayfalı olarak listeler
     * 
     * @param pageable sayfalama bilgisi
     * @return sayfalı aktif station listesi
     */
    Page<Station> findByActiveTrue(Pageable pageable);

    /**
     * Station name ile station arar (case insensitive)
     * 
     * @param stationName station name
     * @return optional station
     */
    Optional<Station> findByStationNameIgnoreCase(String stationName);

    /**
     * Station code ile station'ın var olup olmadığını kontrol eder
     * 
     * @param stationCode station code
     * @return true if exists, false otherwise
     */
    boolean existsByStationCode(String stationCode);

    /**
     * Station name ile station'ın var olup olmadığını kontrol eder (case insensitive)
     * 
     * @param stationName station name
     * @return true if exists, false otherwise
     */
    boolean existsByStationNameIgnoreCase(String stationName);

    /**
     * Country ile station'ları listeler
     * 
     * @param country country
     * @return station listesi
     */
    List<Station> findByCountryAndActiveTrue(String country);

    /**
     * City ile station'ları listeler
     * 
     * @param city city
     * @return station listesi
     */
    List<Station> findByCityAndActiveTrue(String city);

    /**
     * Timezone ile station'ları listeler
     * 
     * @param timezone timezone
     * @return station listesi
     */
    List<Station> findByTimezoneAndActiveTrue(String timezone);

    /**
     * Station name ile benzer station'ları arar
     * 
     * @param stationName station name
     * @return station listesi
     */
    @Query("SELECT s FROM Station s WHERE LOWER(s.stationName) LIKE LOWER(CONCAT('%', :stationName, '%')) AND s.active = true")
    List<Station> findByStationNameContainingIgnoreCaseAndActiveTrue(@Param("stationName") String stationName);

    /**
     * Station code ile benzer station'ları arar
     * 
     * @param stationCode station code
     * @return station listesi
     */
    @Query("SELECT s FROM Station s WHERE LOWER(s.stationCode) LIKE LOWER(CONCAT('%', :stationCode, '%')) AND s.active = true")
    List<Station> findByStationCodeContainingIgnoreCaseAndActiveTrue(@Param("stationCode") String stationCode);

    /**
     * City ile benzer station'ları arar
     * 
     * @param city city
     * @return station listesi
     */
    @Query("SELECT s FROM Station s WHERE LOWER(s.city) LIKE LOWER(CONCAT('%', :city, '%')) AND s.active = true")
    List<Station> findByCityContainingIgnoreCaseAndActiveTrue(@Param("city") String city);

    /**
     * Country ile benzer station'ları arar
     * 
     * @param country country
     * @return station listesi
     */
    @Query("SELECT s FROM Station s WHERE LOWER(s.country) LIKE LOWER(CONCAT('%', :country, '%')) AND s.active = true")
    List<Station> findByCountryContainingIgnoreCaseAndActiveTrue(@Param("country") String country);

    /**
     * Belirli bir bölgedeki station'ları arar (latitude/longitude aralığında)
     * 
     * @param minLat minimum latitude
     * @param maxLat maximum latitude
     * @param minLon minimum longitude
     * @param maxLon maximum longitude
     * @return station listesi
     */
    @Query("SELECT s FROM Station s WHERE s.latitude BETWEEN :minLat AND :maxLat AND s.longitude BETWEEN :minLon AND :maxLon AND s.active = true")
    List<Station> findByLocationBetweenAndActiveTrue(@Param("minLat") Double minLat, @Param("maxLat") Double maxLat, 
                                                   @Param("minLon") Double minLon, @Param("maxLon") Double maxLon);
} 