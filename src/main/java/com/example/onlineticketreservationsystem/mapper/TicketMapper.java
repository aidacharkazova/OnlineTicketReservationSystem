package com.example.onlineticketreservationsystem.mapper;


import com.example.onlineticketreservationsystem.dto.request.TicketRequest;
import com.example.onlineticketreservationsystem.dto.response.TicketResponse;
import com.example.onlineticketreservationsystem.model.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {


    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "seat.seatNumber", target = "seatNumber")
    TicketResponse toResponse(Ticket ticket);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "schedule", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "seat", ignore = true)
    @Mapping(target = "bookingTime", ignore = true)
    Ticket toEntity(TicketRequest ticketRequest);


}
