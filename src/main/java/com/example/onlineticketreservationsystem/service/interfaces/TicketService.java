package com.example.onlineticketreservationsystem.service.interfaces;

import com.example.onlineticketreservationsystem.dto.request.TicketRequest;
import com.example.onlineticketreservationsystem.dto.response.TicketResponse;

import java.util.List;

public interface TicketService {
    TicketResponse reserveTicket(TicketRequest request);
    List<TicketResponse> getAllTickets();
}
