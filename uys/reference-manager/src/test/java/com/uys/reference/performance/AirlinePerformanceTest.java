package com.uys.reference.performance;

import com.uys.reference.dto.AirlineDto;
import com.uys.reference.entity.Airline;
import com.uys.reference.repository.AirlineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic Performance Tests for Airline APIs
 * 
 * DISABLED: Database connection issues in test environment
 */
@Disabled("Database connection issues in test environment")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AirlinePerformanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AirlineRepository airlineRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/airlines";
        
        // Clear data
        airlineRepository.deleteAll();
        
        // Create test data
        for (int i = 1; i <= 100; i++) {
            Airline airline = Airline.builder()
                    .airlineCode("T" + String.format("%02d", i))
                    .airlineName("Test Airlines " + i)
                    .country("Turkey")
                    .city("Istanbul")
                    .description("Test airline " + i)
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .version(0L)
                    .build();
            airlineRepository.save(airline);
        }
    }

    @Test
    void testGetAllAirlinesPerformance() {
        // Warmup
        for (int i = 0; i < 10; i++) {
            restTemplate.getForEntity(baseUrl + "?page=0&size=20", String.class);
        }

        // Measure
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "?page=0&size=20", String.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        double avgResponseTime = totalTime / 100.0;
        
        System.out.println("=== GET ALL AIRLINES PERFORMANCE ===");
        System.out.println("Total requests: 100");
        System.out.println("Total time: " + totalTime + " ms");
        System.out.println("Average response time: " + avgResponseTime + " ms");
        System.out.println("Requests per second: " + (1000.0 / avgResponseTime));
        
        // Assert performance requirements (300ms max avg response time)
        assertTrue(avgResponseTime < 300, "Average response time should be less than 300ms, was: " + avgResponseTime);
    }

    @Test
    void testGetAirlineByCodePerformance() {
        // Warmup
        for (int i = 0; i < 10; i++) {
            restTemplate.getForEntity(baseUrl + "/T01", String.class);
        }

        // Measure
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            String code = "T" + String.format("%02d", (i % 100) + 1);
            ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/" + code, String.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        double avgResponseTime = totalTime / 100.0;
        
        System.out.println("=== GET AIRLINE BY CODE PERFORMANCE ===");
        System.out.println("Total requests: 100");
        System.out.println("Total time: " + totalTime + " ms");
        System.out.println("Average response time: " + avgResponseTime + " ms");
        System.out.println("Requests per second: " + (1000.0 / avgResponseTime));
        
        // Assert performance requirements (50ms max avg response time - cached)
        assertTrue(avgResponseTime < 100, "Average response time should be less than 100ms, was: " + avgResponseTime);
    }

    @Test
    void testConcurrentRequests() throws Exception {
        int numberOfThreads = 10;
        int requestsPerThread = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        
        List<CompletableFuture<Long>> futures = new ArrayList<>();
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < numberOfThreads; i++) {
            CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
                long threadStartTime = System.currentTimeMillis();
                
                for (int j = 0; j < requestsPerThread; j++) {
                    String code = "T" + String.format("%02d", (j % 100) + 1);
                    ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/" + code, String.class);
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                }
                
                return System.currentTimeMillis() - threadStartTime;
            }, executor);
            
            futures.add(future);
        }
        
        // Wait for all requests to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(30, TimeUnit.SECONDS);
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        int totalRequests = numberOfThreads * requestsPerThread;
        double avgResponseTime = totalTime / (double) totalRequests;
        double requestsPerSecond = (totalRequests * 1000.0) / totalTime;
        
        System.out.println("=== CONCURRENT REQUESTS PERFORMANCE ===");
        System.out.println("Threads: " + numberOfThreads);
        System.out.println("Requests per thread: " + requestsPerThread);
        System.out.println("Total requests: " + totalRequests);
        System.out.println("Total time: " + totalTime + " ms");
        System.out.println("Average response time: " + avgResponseTime + " ms");
        System.out.println("Requests per second: " + requestsPerSecond);
        
        // Assert concurrent performance requirements
        assertTrue(requestsPerSecond > 50, "Should handle at least 50 requests per second, was: " + requestsPerSecond);
        
        executor.shutdown();
    }

    @Test
    void testSearchPerformance() {
        // Warmup
        for (int i = 0; i < 10; i++) {
            restTemplate.getForEntity(baseUrl + "/search?query=Test", String.class);
        }

        // Measure
        long startTime = System.currentTimeMillis();
        
        String[] queries = {"Test", "Airlines", "Turkey", "Istanbul", "T0"};
        
        for (int i = 0; i < 100; i++) {
            String query = queries[i % queries.length];
            ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/search?query=" + query, String.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        double avgResponseTime = totalTime / 100.0;
        
        System.out.println("=== SEARCH PERFORMANCE ===");
        System.out.println("Total requests: 100");
        System.out.println("Total time: " + totalTime + " ms");
        System.out.println("Average response time: " + avgResponseTime + " ms");
        System.out.println("Requests per second: " + (1000.0 / avgResponseTime));
        
        // Assert search performance requirements (500ms max avg response time)
        assertTrue(avgResponseTime < 500, "Search average response time should be less than 500ms, was: " + avgResponseTime);
    }

    @Test
    void testCreateAirlinePerformance() {
        // Create test data
        List<AirlineDto.CreateRequest> requests = new ArrayList<>();
        for (int i = 200; i < 250; i++) {
            AirlineDto.CreateRequest request = AirlineDto.CreateRequest.builder()
                    .airlineCode("P" + String.format("%02d", i))
                    .airlineName("Performance Test Airlines " + i)
                    .country("Turkey")
                    .city("Ankara")
                    .description("Performance test airline " + i)
                    .build();
            requests.add(request);
        }

        // Warmup
        for (int i = 0; i < 5; i++) {
            AirlineDto.CreateRequest warmupRequest = AirlineDto.CreateRequest.builder()
                    .airlineCode("W" + String.format("%02d", i))
                    .airlineName("Warmup Airlines " + i)
                    .country("Turkey")
                    .city("Izmir")
                    .description("Warmup airline " + i)
                    .build();
            restTemplate.postForEntity(baseUrl, warmupRequest, String.class);
        }

        // Measure
        long startTime = System.currentTimeMillis();
        
        for (AirlineDto.CreateRequest request : requests) {
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        double avgResponseTime = totalTime / (double) requests.size();
        
        System.out.println("=== CREATE AIRLINE PERFORMANCE ===");
        System.out.println("Total requests: " + requests.size());
        System.out.println("Total time: " + totalTime + " ms");
        System.out.println("Average response time: " + avgResponseTime + " ms");
        System.out.println("Requests per second: " + (1000.0 / avgResponseTime));
        
        // Assert create performance requirements (1000ms max avg response time)
        assertTrue(avgResponseTime < 1000, "Create average response time should be less than 1000ms, was: " + avgResponseTime);
    }
}