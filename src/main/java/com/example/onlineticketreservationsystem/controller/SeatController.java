package com.example.onlineticketreservationsystem.controller;


import com.example.onlineticketreservationsystem.dto.request.SeatRequest;
import com.example.onlineticketreservationsystem.dto.response.SeatResponse;
import com.example.onlineticketreservationsystem.service.interfaces.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @PostMapping("/venue/{venueId}")
    public ResponseEntity<SeatResponse> createSeat(@PathVariable Long venueId, @Valid @RequestBody SeatRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seatService.createSeat(venueId, request));
    }

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<SeatResponse>> getSeatsByVenue(@PathVariable Long venueId) {
        return ResponseEntity.ok(seatService.getSeatsByVenue(venueId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatResponse> getSeatById(@PathVariable Long id) {
        return ResponseEntity.ok(seatService.getSeatById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }
}
