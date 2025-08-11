package com.uys.reference.mapper;

import com.uys.reference.dto.StationDto;
import com.uys.reference.entity.Station;
import org.mapstruct.*;

import java.util.List;

/**
 * Station MapStruct Mapper - Entity ve DTO arasında dönüşüm yapar
 * 
 * @author UYS Development Team
 * @version 1.0.0
 * @since 2024-07-30
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StationMapper {

    /**
     * Entity'den Response DTO'ya dönüştürür
     * 
     * @param station entity
     * @return response dto
     */
    StationDto.Response toResponse(Station station);

    /**
     * Entity listesinden Response DTO listesine dönüştürür
     * 
     * @param stations entity listesi
     * @return response dto listesi
     */
    List<StationDto.Response> toResponseList(List<Station> stations);

    /**
     * Entity'den List Response DTO'ya dönüştürür
     * 
     * @param station entity
     * @return list response dto
     */
    StationDto.ListResponse toListResponse(Station station);

    /**
     * Entity listesinden List Response DTO listesine dönüştürür
     * 
     * @param stations entity listesi
     * @return list response dto listesi
     */
    List<StationDto.ListResponse> toListResponseList(List<Station> stations);

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
    Station toEntity(StationDto.CreateRequest createRequest);

    /**
     * Update Request DTO'dan Entity'ye dönüştürür
     * 
     * @param updateRequest update request dto
     * @param station mevcut entity
     * @return güncellenmiş entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stationCode", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromUpdateRequest(StationDto.UpdateRequest updateRequest, @MappingTarget Station station);
} 