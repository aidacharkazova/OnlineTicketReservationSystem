package com.example.onlineticketreservationsystem.service.impl;

import com.example.onlineticketreservationsystem.dto.request.EventRequest;
import com.example.onlineticketreservationsystem.dto.response.EventResponse;
import com.example.onlineticketreservationsystem.exception.custom.ResourceNotFoundException;
import com.example.onlineticketreservationsystem.mapper.EventMapper;
import com.example.onlineticketreservationsystem.model.entity.Event;
import com.example.onlineticketreservationsystem.repository.EventRepository;
import com.example.onlineticketreservationsystem.service.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public EventResponse createEvent(EventRequest eventRequest) {
        Event entity = eventMapper.toEntity(eventRequest);
        eventRepository.save(entity);
        return eventMapper.toResponse(entity);

    }

    @Override
    public List<EventResponse> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(event -> eventMapper.toResponse(event)).toList();
    }

    @Override
    public EventResponse getEventById(long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("There is no event!"));
        return eventMapper.toResponse(event);
    }

    @Override
    public EventResponse updateEvent(long id, EventRequest eventRequest) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("There is no event!"));
        event.setName(eventRequest.getName());
        event.setDescription(eventRequest.getDescription());
        event.setType(eventRequest.getType());
        eventRepository.save(event);
        return eventMapper.toResponse(event);
    }

    @Override
    public void deleteEvent(EventRequest eventRequest) {
        eventRepository.delete(eventMapper.toEntity(eventRequest));

    }
}
