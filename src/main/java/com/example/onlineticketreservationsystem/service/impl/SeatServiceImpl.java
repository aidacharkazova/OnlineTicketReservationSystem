package com.example.onlineticketreservationsystem.service.impl;

import com.example.onlineticketreservationsystem.dto.request.SeatRequest;
import com.example.onlineticketreservationsystem.dto.response.SeatResponse;
import com.example.onlineticketreservationsystem.exception.custom.ResourceNotFoundException;
import com.example.onlineticketreservationsystem.mapper.SeatMapper;
import com.example.onlineticketreservationsystem.model.entity.Seat;
import com.example.onlineticketreservationsystem.model.entity.Venue;
import com.example.onlineticketreservationsystem.repository.SeatRepository;
import com.example.onlineticketreservationsystem.repository.VenueRepository;
import com.example.onlineticketreservationsystem.service.interfaces.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;
    private final VenueRepository venueRepository;
    @Override
    public SeatResponse createSeat(Long venueId, SeatRequest request) {
        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new ResourceNotFoundException("There is no venue with id " + venueId));
        Seat seat = seatMapper.toEntity(request);
        seat.setVenue(venue);
        return seatMapper.toResponse(seatRepository.save(seat));
    }

    @Override
    public List<SeatResponse> getSeatsByVenue(Long venueId) {
        venueRepository.findById(venueId).orElseThrow(() -> new ResourceNotFoundException("There is no venue with id " + venueId));
        seatRepository.findAll().stream().filter(seat -> seat.getVenue().getId().equals(venueId)).toList();
        return null;
    }

    @Override
    public SeatResponse getSeatById(Long seatId) {
        return null;
    }

    @Override
    public void deleteSeat(Long seatId) {

    }
}
