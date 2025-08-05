package com.uys.reference.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Liquibase Migration Integration Test
 * 
 * Bu test sınıfı Liquibase migration'larının doğru çalıştığını ve
 * veritabanı şemasının beklenen şekilde oluşturulduğunu doğrular.
 */
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.liquibase.enabled=true",
    "spring.jpa.hibernate.ddl-auto=validate"
})
class LiquibaseMigrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("uys_reference_test")
            .withUsername("uys_user")
            .withPassword("uys_password")
            .withInitScript("init-test-db.sql");

    @Test
    void testLiquibaseMigration_ShouldCreateAllTables() throws Exception {
        // Given: Spring Boot context is loaded with Liquibase enabled
        
        // When: Application starts and Liquibase runs
        
        // Then: All tables should be created
        try (Connection connection = mysql.createConnection("");
             Statement statement = connection.createStatement()) {
            
            // Check if airline table exists
            ResultSet airlineResult = statement.executeQuery(
                "SELECT COUNT(*) FROM information_schema.tables " +
                "WHERE table_schema = 'uys_reference_test' AND table_name = 'airline'"
            );
            airlineResult.next();
            assertEquals(1, airlineResult.getInt(1), "Airline table should exist");
            
            // Check if aircraft table exists
            ResultSet aircraftResult = statement.executeQuery(
                "SELECT COUNT(*) FROM information_schema.tables " +
                "WHERE table_schema = 'uys_reference_test' AND table_name = 'aircraft'"
            );
            aircraftResult.next();
            assertEquals(1, aircraftResult.getInt(1), "Aircraft table should exist");
            
            // Check if station table exists
            ResultSet stationResult = statement.executeQuery(
                "SELECT COUNT(*) FROM information_schema.tables " +
                "WHERE table_schema = 'uys_reference_test' AND table_name = 'station'"
            );
            stationResult.next();
            assertEquals(1, stationResult.getInt(1), "Station table should exist");
        }
    }

    @Test
    void testLiquibaseMigration_ShouldInsertInitialData() throws Exception {
        // Given: Spring Boot context is loaded with Liquibase enabled
        
        // When: Application starts and Liquibase runs
        
        // Then: Initial data should be inserted
        try (Connection connection = mysql.createConnection("");
             Statement statement = connection.createStatement()) {
            
            // Check airline data
            ResultSet airlineCount = statement.executeQuery("SELECT COUNT(*) FROM airline");
            airlineCount.next();
            assertTrue(airlineCount.getInt(1) > 0, "Should have initial airline data");
            
            // Check specific airline
            ResultSet tkAirline = statement.executeQuery(
                "SELECT airline_code, airline_name FROM airline WHERE airline_code = 'TK'"
            );
            assertTrue(tkAirline.next(), "Turkish Airlines should exist");
            assertEquals("Turkish Airlines", tkAirline.getString("airline_name"));
            
            // Check station data
            ResultSet stationCount = statement.executeQuery("SELECT COUNT(*) FROM station");
            stationCount.next();
            assertTrue(stationCount.getInt(1) > 0, "Should have initial station data");
            
            // Check specific station
            ResultSet istStation = statement.executeQuery(
                "SELECT station_code, station_name FROM station WHERE station_code = 'IST'"
            );
            assertTrue(istStation.next(), "Istanbul Airport should exist");
            assertEquals("Istanbul Airport", istStation.getString("station_name"));
            
            // Check aircraft data
            ResultSet aircraftCount = statement.executeQuery("SELECT COUNT(*) FROM aircraft");
            aircraftCount.next();
            assertTrue(aircraftCount.getInt(1) > 0, "Should have initial aircraft data");
            
            // Check specific aircraft
            ResultSet tcJreAircraft = statement.executeQuery(
                "SELECT registration, aircraft_type FROM aircraft WHERE registration = 'TC-JRE'"
            );
            assertTrue(tcJreAircraft.next(), "TC-JRE aircraft should exist");
            assertEquals("A320", tcJreAircraft.getString("aircraft_type"));
        }
    }

    @Test
    void testLiquibaseMigration_ShouldCreateIndexes() throws Exception {
        // Given: Spring Boot context is loaded with Liquibase enabled
        
        // When: Application starts and Liquibase runs
        
        // Then: Indexes should be created
        try (Connection connection = mysql.createConnection("");
             Statement statement = connection.createStatement()) {
            
            // Check airline indexes
            ResultSet airlineIndexes = statement.executeQuery(
                "SELECT COUNT(*) FROM information_schema.statistics " +
                "WHERE table_schema = 'uys_reference_test' " +
                "AND table_name = 'airline' " +
                "AND index_name LIKE 'idx_airline_%'"
            );
            airlineIndexes.next();
            assertTrue(airlineIndexes.getInt(1) > 0, "Should have airline indexes");
            
            // Check aircraft indexes
            ResultSet aircraftIndexes = statement.executeQuery(
                "SELECT COUNT(*) FROM information_schema.statistics " +
                "WHERE table_schema = 'uys_reference_test' " +
                "AND table_name = 'aircraft' " +
                "AND index_name LIKE 'idx_aircraft_%'"
            );
            aircraftIndexes.next();
            assertTrue(aircraftIndexes.getInt(1) > 0, "Should have aircraft indexes");
            
            // Check station indexes
            ResultSet stationIndexes = statement.executeQuery(
                "SELECT COUNT(*) FROM information_schema.statistics " +
                "WHERE table_schema = 'uys_reference_test' " +
                "AND table_name = 'station' " +
                "AND index_name LIKE 'idx_station_%'"
            );
            stationIndexes.next();
            assertTrue(stationIndexes.getInt(1) > 0, "Should have station indexes");
        }
    }

    @Test
    void testLiquibaseMigration_ShouldCreateForeignKeys() throws Exception {
        // Given: Spring Boot context is loaded with Liquibase enabled
        
        // When: Application starts and Liquibase runs
        
        // Then: Foreign keys should be created
        try (Connection connection = mysql.createConnection("");
             Statement statement = connection.createStatement()) {
            
            // Check foreign key constraint
            ResultSet foreignKeys = statement.executeQuery(
                "SELECT COUNT(*) FROM information_schema.key_column_usage " +
                "WHERE table_schema = 'uys_reference_test' " +
                "AND table_name = 'aircraft' " +
                "AND constraint_name = 'fk_aircraft_airline'"
            );
            foreignKeys.next();
            assertEquals(1, foreignKeys.getInt(1), "Should have aircraft-airline foreign key");
        }
    }
} 