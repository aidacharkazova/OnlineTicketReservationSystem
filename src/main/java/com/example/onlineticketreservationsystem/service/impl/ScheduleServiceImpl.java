package com.example.onlineticketreservationsystem.service.impl;

import com.example.onlineticketreservationsystem.dto.request.ScheduleRequest;
import com.example.onlineticketreservationsystem.dto.response.ScheduleResponse;
import com.example.onlineticketreservationsystem.exception.custom.ResourceNotFoundException;
import com.example.onlineticketreservationsystem.mapper.ScheduleMapper;
import com.example.onlineticketreservationsystem.model.entity.Event;
import com.example.onlineticketreservationsystem.model.entity.Schedule;
import com.example.onlineticketreservationsystem.model.entity.Venue;
import com.example.onlineticketreservationsystem.repository.EventRepository;
import com.example.onlineticketreservationsystem.repository.ScheduleRepository;
import com.example.onlineticketreservationsystem.repository.VenueRepository;
import com.example.onlineticketreservationsystem.service.interfaces.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final ScheduleMapper scheduleMapper;
    private static final String CACHE_NAME = "schedules";
    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    @Override
    public ScheduleResponse createSchedule(ScheduleRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));

        Schedule schedule = scheduleMapper.toEntity(request);
        schedule.setEvent(event);
        schedule.setVenue(venue);

        return scheduleMapper.toResponse(scheduleRepository.save(schedule));
    }

    @Cacheable(value = CACHE_NAME, key = "'all'")
    @Override
    public List<ScheduleResponse> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(scheduleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CACHE_NAME, key = "#id")
    @Override
    public ScheduleResponse getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        return scheduleMapper.toResponse(schedule);
    }

    @Override
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("" +
                    "Schedule not found");
        }
        scheduleRepository.deleteById(id);
    }
}
