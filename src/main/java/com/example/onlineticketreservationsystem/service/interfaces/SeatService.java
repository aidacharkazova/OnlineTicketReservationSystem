package com.example.onlineticketreservationsystem.service.interfaces;

import com.example.onlineticketreservationsystem.dto.request.SeatRequest;
import com.example.onlineticketreservationsystem.dto.response.SeatResponse;
import com.example.onlineticketreservationsystem.model.entity.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatService {

    SeatResponse createSeat(Long venueId, SeatRequest request);
    List<SeatResponse> getSeatsByVenue(Long venueId);
    SeatResponse getSeatById(Long seatId);
    void deleteSeat(Long seatId);




}
