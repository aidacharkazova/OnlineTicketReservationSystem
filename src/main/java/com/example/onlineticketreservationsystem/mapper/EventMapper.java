package com.example.onlineticketreservationsystem.mapper;

import com.example.onlineticketreservationsystem.dto.request.EventRequest;
import com.example.onlineticketreservationsystem.dto.response.EventResponse;
import com.example.onlineticketreservationsystem.model.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ScheduleMapper.class})
public interface EventMapper {

    EventResponse toResponse(Event event);

    @Mapping(target = "schedules", ignore = true)
    Event toEntity(EventRequest eventRequest);
}
