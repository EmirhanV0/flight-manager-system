package com.uys.archive.mapper;

import com.uys.archive.dto.EventLogDto;
import com.uys.archive.entity.EventLog;
import org.mapstruct.*;

import java.util.List;

/**
 * Event Log Mapper
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventLogMapper {

    EventLogDto.Response toResponse(EventLog eventLog);
    
    List<EventLogDto.Response> toResponseList(List<EventLog> eventLogs);
    
    EventLogDto.ListResponse toListResponse(EventLog eventLog);
    
    List<EventLogDto.ListResponse> toListResponseList(List<EventLog> eventLogs);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "archivedAt", ignore = true)
    EventLog toEntity(EventLogDto.CreateRequest createRequest);
}