package com.uys.reference.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uys.reference.entity.Airline;
import com.uys.reference.event.dto.AirlineEventDto;
import com.uys.reference.event.enums.EventType;
import com.uys.reference.event.publisher.EventPublisherService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Kafka event publishing
 * Uses Testcontainers for real Kafka testing
 */
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class KafkaIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("uys_reference_test")
            .withUsername("uys_user")
            .withPassword("uys_password");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.cloud.stream.kafka.binder.brokers", kafka::getBootstrapServers);
    }

    @Autowired
    private EventPublisherService eventPublisherService;

    @Autowired
    private ObjectMapper objectMapper;

    private KafkaConsumer<String, String> testConsumer;
    private Airline testAirline;

    @BeforeEach
    void setUp() {
        // Setup Kafka consumer for testing
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-" + UUID.randomUUID());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        testConsumer = new KafkaConsumer<>(props);
        testConsumer.subscribe(Collections.singletonList("airline-events"));

        // Create test airline
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
    }

    @Test
    void publishAirlineEvent_Created_Success() throws Exception {
        // When
        eventPublisherService.publishAirlineEvent(testAirline, EventType.CREATED);

        // Then
        ConsumerRecords<String, String> records = testConsumer.poll(Duration.ofSeconds(10));
        assertFalse(records.isEmpty(), "Should receive at least one message");

        ConsumerRecord<String, String> record = records.iterator().next();
        assertNotNull(record.value());

        // Parse the event
        AirlineEventDto eventDto = objectMapper.readValue(record.value(), AirlineEventDto.class);
        
        assertEquals("CREATED", eventDto.getEventType());
        assertEquals("TK", eventDto.getAirlineCode());
        assertEquals("Turkish Airlines", eventDto.getAirlineName());
        assertEquals("Turkey", eventDto.getCountry());
        assertEquals("Istanbul", eventDto.getCity());
        assertTrue(eventDto.getActive());
        assertEquals("reference-manager", eventDto.getSourceService());
        assertEquals("1.0", eventDto.getVersion());
        assertNotNull(eventDto.getEventId());
        assertNotNull(eventDto.getEventTimestamp());
    }

    @Test
    void publishAirlineEvent_Updated_Success() throws Exception {
        // When
        eventPublisherService.publishAirlineEvent(testAirline, EventType.UPDATED);

        // Then
        ConsumerRecords<String, String> records = testConsumer.poll(Duration.ofSeconds(10));
        assertFalse(records.isEmpty());

        ConsumerRecord<String, String> record = records.iterator().next();
        AirlineEventDto eventDto = objectMapper.readValue(record.value(), AirlineEventDto.class);
        
        assertEquals("UPDATED", eventDto.getEventType());
        assertEquals("TK", eventDto.getAirlineCode());
    }

    @Test
    void publishAirlineEvent_Deleted_Success() throws Exception {
        // When
        eventPublisherService.publishAirlineEvent(testAirline, EventType.DELETED);

        // Then
        ConsumerRecords<String, String> records = testConsumer.poll(Duration.ofSeconds(10));
        assertFalse(records.isEmpty());

        ConsumerRecord<String, String> record = records.iterator().next();
        AirlineEventDto eventDto = objectMapper.readValue(record.value(), AirlineEventDto.class);
        
        assertEquals("DELETED", eventDto.getEventType());
        assertEquals("TK", eventDto.getAirlineCode());
    }

    @Test
    void publishAirlineEvent_StatusChanged_Success() throws Exception {
        // Given
        testAirline.setActive(false);

        // When
        eventPublisherService.publishAirlineEvent(testAirline, EventType.STATUS_CHANGED);

        // Then
        ConsumerRecords<String, String> records = testConsumer.poll(Duration.ofSeconds(10));
        assertFalse(records.isEmpty());

        ConsumerRecord<String, String> record = records.iterator().next();
        AirlineEventDto eventDto = objectMapper.readValue(record.value(), AirlineEventDto.class);
        
        assertEquals("STATUS_CHANGED", eventDto.getEventType());
        assertEquals("TK", eventDto.getAirlineCode());
        assertFalse(eventDto.getActive());
    }

    @Test
    void publishMultipleEvents_AllReceived() throws Exception {
        // When
        eventPublisherService.publishAirlineEvent(testAirline, EventType.CREATED);
        eventPublisherService.publishAirlineEvent(testAirline, EventType.UPDATED);
        eventPublisherService.publishAirlineEvent(testAirline, EventType.STATUS_CHANGED);

        // Then
        ConsumerRecords<String, String> records = testConsumer.poll(Duration.ofSeconds(10));
        assertTrue(records.count() >= 3, "Should receive at least 3 messages");

        int createdCount = 0, updatedCount = 0, statusChangedCount = 0;

        for (ConsumerRecord<String, String> record : records) {
            AirlineEventDto eventDto = objectMapper.readValue(record.value(), AirlineEventDto.class);
            switch (eventDto.getEventType()) {
                case "CREATED" -> createdCount++;
                case "UPDATED" -> updatedCount++;
                case "STATUS_CHANGED" -> statusChangedCount++;
            }
        }

        assertEquals(1, createdCount);
        assertEquals(1, updatedCount);
        assertEquals(1, statusChangedCount);
    }

    @Test
    void eventMessage_HasCorrectHeaders() throws Exception {
        // When
        eventPublisherService.publishAirlineEvent(testAirline, EventType.CREATED);

        // Then
        ConsumerRecords<String, String> records = testConsumer.poll(Duration.ofSeconds(10));
        assertFalse(records.isEmpty());

        ConsumerRecord<String, String> record = records.iterator().next();
        
        // Check headers (if any are set by the producer)
        assertNotNull(record.key()); // Should have a key
        assertEquals("airline-events", record.topic());
        assertTrue(record.offset() >= 0);
        assertTrue(record.timestamp() > 0);
    }

    @Test
    void eventSerialization_JsonFormat_Valid() throws Exception {
        // When
        eventPublisherService.publishAirlineEvent(testAirline, EventType.CREATED);

        // Then
        ConsumerRecords<String, String> records = testConsumer.poll(Duration.ofSeconds(10));
        assertFalse(records.isEmpty());

        ConsumerRecord<String, String> record = records.iterator().next();
        String jsonMessage = record.value();
        
        // Verify it's valid JSON
        assertDoesNotThrow(() -> objectMapper.readTree(jsonMessage));
        
        // Verify it can be deserialized back to our DTO
        assertDoesNotThrow(() -> objectMapper.readValue(jsonMessage, AirlineEventDto.class));
    }
}