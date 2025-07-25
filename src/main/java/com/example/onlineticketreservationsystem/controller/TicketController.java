package com.example.onlineticketreservationsystem.controller;


import com.example.onlineticketreservationsystem.dto.request.TicketRequest;
import com.example.onlineticketreservationsystem.dto.response.TicketResponse;
import com.example.onlineticketreservationsystem.service.interfaces.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> reserveTicket(@Valid @RequestBody TicketRequest request) {
        return ResponseEntity.status(201).body(ticketService.reserveTicket(request));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketResponse>> getTicketsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUserId(userId));
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> cancelTicket(@PathVariable Long ticketId) {
        ticketService.cancelTicket(ticketId);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/cache/{id}")
    public ResponseEntity<Void> deleteCache(@PathVariable Long id) {
        ticketService.deleteFromCache(id);
        return ResponseEntity.noContent().build();
    }
}
