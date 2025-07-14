package com.example.onlineticketreservationsystem.mapper;


import com.example.onlineticketreservationsystem.dto.request.ScheduleRequest;
import com.example.onlineticketreservationsystem.dto.response.ScheduleResponse;
import com.example.onlineticketreservationsystem.model.entity.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TicketMapper.class})
public interface ScheduleMapper {

    @Mapping(source = "event.name", target = "eventName")
    @Mapping(source = "venue.name", target = "venueName")
    ScheduleResponse toResponse(Schedule schedule);

    @Mapping(target = "event", ignore = true)
    @Mapping(target = "venue", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    Schedule toEntity(ScheduleRequest scheduleRequest);
}
