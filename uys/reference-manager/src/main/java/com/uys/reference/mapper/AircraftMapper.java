package com.uys.reference.mapper;

import com.uys.reference.dto.AircraftDto;
import com.uys.reference.entity.Aircraft;
import org.mapstruct.*;

import java.util.List;

/**
 * Aircraft MapStruct Mapper - Entity ve DTO arasında dönüşüm yapar
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {AirlineMapper.class})
public interface AircraftMapper {

    /**
     * Entity'den Response DTO'ya dönüştürür
     * 
     * @param aircraft entity
     * @return response dto
     */
    AircraftDto.Response toResponse(Aircraft aircraft);

    /**
     * Entity listesinden Response DTO listesine dönüştürür
     * 
     * @param aircrafts entity listesi
     * @return response dto listesi
     */
    List<AircraftDto.Response> toResponseList(List<Aircraft> aircrafts);

    /**
     * Entity'den List Response DTO'ya dönüştürür
     * 
     * @param aircraft entity
     * @return list response dto
     */
    @Mapping(target = "airlineCode", source = "airline.airlineCode")
    @Mapping(target = "airlineName", source = "airline.airlineName")
    AircraftDto.ListResponse toListResponse(Aircraft aircraft);

    /**
     * Entity listesinden List Response DTO listesine dönüştürür
     * 
     * @param aircrafts entity listesi
     * @return list response dto listesi
     */
    List<AircraftDto.ListResponse> toListResponseList(List<Aircraft> aircrafts);

    /**
     * Create Request DTO'dan Entity'ye dönüştürür
     * 
     * @param createRequest create request dto
     * @return entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Aircraft toEntity(AircraftDto.CreateRequest createRequest);

    /**
     * Update Request DTO'dan Entity'ye dönüştürür
     * 
     * @param updateRequest update request dto
     * @param aircraft mevcut entity
     * @return güncellenmiş entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registration", ignore = true)
    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromUpdateRequest(AircraftDto.UpdateRequest updateRequest, @MappingTarget Aircraft aircraft);
} 