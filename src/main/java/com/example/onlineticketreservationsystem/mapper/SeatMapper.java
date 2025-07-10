package com.example.onlineticketreservationsystem.mapper;


import com.example.onlineticketreservationsystem.dto.request.SeatRequest;
import com.example.onlineticketreservationsystem.dto.response.SeatResponse;
import com.example.onlineticketreservationsystem.model.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface SeatMapper {

    @Mapping(target = "tickets", ignore = true)
    @Mapping(target = "venue", ignore = true)
    Seat toEntity(SeatRequest request);

    SeatResponse toResponse(Seat seat);

}
