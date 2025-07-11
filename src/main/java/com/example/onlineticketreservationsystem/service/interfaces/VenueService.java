package com.example.onlineticketreservationsystem.service.interfaces;


import com.example.onlineticketreservationsystem.dto.request.VenueRequest;
import com.example.onlineticketreservationsystem.dto.response.VenueResponse;

import java.util.List;

public interface VenueService {
    VenueResponse createVenue(VenueRequest request);
    List<VenueResponse> getAllVenues();
    VenueResponse getVenueById(Long id);
    void deleteVenue(Long id);
    VenueResponse updateVenue(Long id, VenueRequest request);
}
