package com.example.onlineticketreservationsystem.service.impl;


import com.example.onlineticketreservationsystem.dto.request.VenueRequest;
import com.example.onlineticketreservationsystem.dto.response.VenueResponse;
import com.example.onlineticketreservationsystem.exception.custom.ResourceNotFoundException;
import com.example.onlineticketreservationsystem.mapper.SeatMapper;
import com.example.onlineticketreservationsystem.mapper.VenueMapper;
import com.example.onlineticketreservationsystem.model.entity.Seat;
import com.example.onlineticketreservationsystem.model.entity.Venue;
import com.example.onlineticketreservationsystem.repository.SeatRepository;
import com.example.onlineticketreservationsystem.repository.VenueRepository;
import com.example.onlineticketreservationsystem.service.interfaces.VenueService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final SeatRepository seatRepository;
    private final VenueMapper venueMapper;
    private final SeatMapper seatMapper;
    private static final String CACHE_NAME = "venues";
    private static final Logger logger = LoggerFactory.getLogger(VenueServiceImpl.class);

    @Override
    public VenueResponse createVenue(VenueRequest request) {
        Venue venue = venueMapper.toEntity(request);

        venue = venueRepository.save(venue);

        Venue finalVenue = venue;
        List<Seat> seats = request.getSeats().stream()
                .map(seatRequest -> {
                    Seat seat = seatMapper.toEntity(seatRequest);
                    seat.setVenue(finalVenue);
                    return seat;
                }).collect(Collectors.toList());

        seatRepository.saveAll(seats);
        venue.setSeats(seats);

        return venueMapper.toResponse(venue);
    }

    @Cacheable(value = CACHE_NAME, key = "'all'")
    @Override
    public List<VenueResponse> getAllVenues() {
        return venueRepository.findAll()
                .stream()
                .map(venueMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CACHE_NAME, key = "#id")
    @Override
    public VenueResponse getVenueById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + id));
        return venueMapper.toResponse(venue);
    }

    @Override
    public void deleteVenue(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + id));
        venueRepository.delete(venue);
    }

    @Override
    public VenueResponse updateVenue(Long id, VenueRequest request) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + id));

        venue.setName(request.getName());
        venue.setAddress(request.getAddress());
        venue.setType(request.getType());


        return venueMapper.toResponse(venueRepository.save(venue));
    }
}
