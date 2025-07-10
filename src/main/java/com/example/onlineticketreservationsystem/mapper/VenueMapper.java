package com.example.onlineticketreservationsystem.mapper;

import com.example.onlineticketreservationsystem.dto.request.VenueRequest;
import com.example.onlineticketreservationsystem.dto.response.VenueResponse;
import com.example.onlineticketreservationsystem.model.entity.Venue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SeatMapper.class, ScheduleMapper.class})
public interface VenueMapper {

    @Mapping(source = "seats", target = "seats")
    @Mapping(source = "schedules", target = "schedules")
    VenueResponse toResponse(Venue venue);

    @Mapping(target = "schedules", ignore = true)
    Venue toEntity(VenueRequest venueRequest);


}
