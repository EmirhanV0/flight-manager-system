package com.uys.reference.mapper;

import com.uys.reference.dto.AirlineDto;
import com.uys.reference.entity.Airline;
import org.mapstruct.*;

import java.util.List;

/**
 * Airline MapStruct Mapper - Entity ve DTO arasında dönüşüm yapar
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AirlineMapper {

    /**
     * Entity'den Response DTO'ya dönüştürür
     * 
     * @param airline entity
     * @return response dto
     */
    AirlineDto.Response toResponse(Airline airline);

    /**
     * Entity listesinden Response DTO listesine dönüştürür
     * 
     * @param airlines entity listesi
     * @return response dto listesi
     */
    List<AirlineDto.Response> toResponseList(List<Airline> airlines);

    /**
     * Entity'den List Response DTO'ya dönüştürür
     * 
     * @param airline entity
     * @return list response dto
     */
    AirlineDto.ListResponse toListResponse(Airline airline);

    /**
     * Entity listesinden List Response DTO listesine dönüştürür
     * 
     * @param airlines entity listesi
     * @return list response dto listesi
     */
    List<AirlineDto.ListResponse> toListResponseList(List<Airline> airlines);

    /**
     * Create Request DTO'dan Entity'ye dönüştürür
     * 
     * @param createRequest create request dto
     * @return entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Airline toEntity(AirlineDto.CreateRequest createRequest);

    /**
     * Update Request DTO'dan Entity'ye dönüştürür
     * 
     * @param updateRequest update request dto
     * @param airline mevcut entity
     * @return güncellenmiş entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "airlineCode", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromUpdateRequest(AirlineDto.UpdateRequest updateRequest, @MappingTarget Airline airline);
} 