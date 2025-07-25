package com.example.onlineticketreservationsystem.service.interfaces;

import com.example.onlineticketreservationsystem.dto.request.EventRequest;
import com.example.onlineticketreservationsystem.dto.response.EventResponse;

import java.util.List;

public interface EventService {
    EventResponse createEvent(EventRequest eventRequest);
    List<EventResponse> getAllEvents();
    EventResponse getEventById(long id);
    EventResponse updateEvent(long id,EventRequest eventRequest);
    void deleteEvent(EventRequest eventRequest);
    void deleteFromCache(long id);
}
