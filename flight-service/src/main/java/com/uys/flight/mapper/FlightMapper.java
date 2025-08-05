package com.uys.flight.mapper;

import com.uys.flight.dto.FlightDto;
import com.uys.flight.entity.Flight;
import org.mapstruct.*;

import java.util.List;

/**
 * Flight Mapper
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FlightMapper {

    FlightDto.Response toResponse(Flight flight);
    
    List<FlightDto.Response> toResponseList(List<Flight> flights);
    
    FlightDto.ListResponse toListResponse(Flight flight);
    
    List<FlightDto.ListResponse> toListResponseList(List<Flight> flights);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "SCHEDULED")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "flightLegs", ignore = true)
    Flight toEntity(FlightDto.CreateRequest createRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flightNumber", ignore = true)
    @Mapping(target = "airlineCode", ignore = true)
    @Mapping(target = "aircraftRegistration", ignore = true)
    @Mapping(target = "departureStationCode", ignore = true)
    @Mapping(target = "arrivalStationCode", ignore = true)
    @Mapping(target = "flightDate", ignore = true)
    @Mapping(target = "scheduledDepartureTime", ignore = true)
    @Mapping(target = "scheduledArrivalTime", ignore = true)
    @Mapping(target = "flightType", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "flightLegs", ignore = true)
    void updateEntityFromUpdateRequest(FlightDto.UpdateRequest updateRequest, @MappingTarget Flight flight);
}