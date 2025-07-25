package com.example.onlineticketreservationsystem.controller;


import com.example.onlineticketreservationsystem.dto.request.EventRequest;
import com.example.onlineticketreservationsystem.dto.response.EventResponse;
import com.example.onlineticketreservationsystem.service.interfaces.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequest request) {
        return ResponseEntity.ok(eventService.updateEvent(id, request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEvent(@RequestBody EventRequest request) {
        eventService.deleteEvent(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cache/{id}")
    public ResponseEntity<Void> deleteCache(@PathVariable Long id) {
        eventService.deleteFromCache(id);
        return ResponseEntity.noContent().build();
    }
}
