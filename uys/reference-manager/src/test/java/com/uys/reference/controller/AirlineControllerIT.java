package com.uys.reference.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uys.reference.dto.AirlineDto;
import com.uys.reference.entity.Airline;
import com.uys.reference.repository.AirlineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for AirlineController
 * Tests the full web layer with real database
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class AirlineControllerIT {



    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Airline testAirline;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Clear existing data
        airlineRepository.deleteAll();

        // Create test data
        testAirline = Airline.builder()
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

        airlineRepository.save(testAirline);
    }

    @Test
    void createAirline_Success() throws Exception {
        // Given
        AirlineDto.CreateRequest createRequest = AirlineDto.CreateRequest.builder()
                .airlineCode("PC")
                .airlineName("Pegasus Airlines")
                .country("Turkey")
                .city("Istanbul")
                .description("Low-cost carrier")
                .build();

        // When & Then
        mockMvc.perform(post("/airlines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.airlineCode").value("PC"))
                .andExpect(jsonPath("$.airlineName").value("Pegasus Airlines"))
                .andExpect(jsonPath("$.country").value("Turkey"))
                .andExpect(jsonPath("$.city").value("Istanbul"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.version").value(0));
    }

    @Test
    void createAirline_DuplicateCode_BadRequest() throws Exception {
        // Given
        AirlineDto.CreateRequest createRequest = AirlineDto.CreateRequest.builder()
                .airlineCode("TK") // Duplicate code
                .airlineName("Test Airlines")
                .country("Test Country")
                .city("Test City")
                .description("Test description")
                .build();

        // When & Then
        mockMvc.perform(post("/airlines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAirline_InvalidData_BadRequest() throws Exception {
        // Given - Missing required fields
        AirlineDto.CreateRequest createRequest = AirlineDto.CreateRequest.builder()
                .airlineCode("") // Empty code
                .airlineName("Test Airlines")
                .build();

        // When & Then
        mockMvc.perform(post("/airlines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAirlineByCode_Success() throws Exception {
        // When & Then
        mockMvc.perform(get("/airlines/{code}", "TK"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.airlineCode").value("TK"))
                .andExpect(jsonPath("$.airlineName").value("Turkish Airlines"))
                .andExpect(jsonPath("$.country").value("Turkey"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void getAirlineByCode_NotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/airlines/{code}", "XX"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAirline_Success() throws Exception {
        // Given
        AirlineDto.UpdateRequest updateRequest = AirlineDto.UpdateRequest.builder()
                .airlineName("Turkish Airlines Updated")
                .country("Turkey")
                .city("Istanbul")
                .description("Updated description")
                .build();

        // When & Then
        mockMvc.perform(put("/airlines/{code}", "TK")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.airlineCode").value("TK"))
                .andExpect(jsonPath("$.airlineName").value("Turkish Airlines Updated"))
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    void updateAirline_NotFound() throws Exception {
        // Given
        AirlineDto.UpdateRequest updateRequest = AirlineDto.UpdateRequest.builder()
                .airlineName("Test Airlines")
                .country("Test Country")
                .city("Test City")
                .build();

        // When & Then
        mockMvc.perform(put("/airlines/{code}", "XX")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAirline_Success() throws Exception {
        // When & Then
        mockMvc.perform(delete("/airlines/{code}", "TK"))
                .andDo(print())
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/airlines/{code}", "TK"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAirline_NotFound() throws Exception {
        // When & Then
        mockMvc.perform(delete("/airlines/{code}", "XX"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void changeAirlineStatus_Success() throws Exception {
        // When & Then
        mockMvc.perform(patch("/airlines/{code}/status", "TK")
                        .param("active", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.airlineCode").value("TK"))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void getAllAirlines_Success() throws Exception {
        // When & Then
        mockMvc.perform(get("/airlines")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].airlineCode").value("TK"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void getActiveAirlines_Success() throws Exception {
        // When & Then
        mockMvc.perform(get("/airlines/active"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].airlineCode").value("TK"))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void searchAirlines_Success() throws Exception {
        // When & Then
        mockMvc.perform(get("/airlines/search")
                        .param("query", "Turkish"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].airlineCode").value("TK"));
    }

    @Test
    void searchAirlines_NoResults() throws Exception {
        // When & Then
        mockMvc.perform(get("/airlines/search")
                        .param("query", "NonExistent"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getAirlinesByCountry_Success() throws Exception {
        // When & Then
        mockMvc.perform(get("/airlines/country/{country}", "Turkey"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].airlineCode").value("TK"));
    }

    @Test
    void getAllAirlines_WithSorting_Success() throws Exception {
        // Given - Add another airline for sorting test
        Airline pegasus = Airline.builder()
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
        airlineRepository.save(pegasus);

        // When & Then
        mockMvc.perform(get("/airlines")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "airlineName,asc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].airlineCode").value("PC")) // Pegasus comes first alphabetically
                .andExpect(jsonPath("$.content[1].airlineCode").value("TK"));
    }
}