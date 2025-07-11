package com.example.onlineticketreservationsystem.service.interfaces;

import com.example.onlineticketreservationsystem.dto.request.TicketRequest;
import com.example.onlineticketreservationsystem.dto.response.TicketResponse;
import com.example.onlineticketreservationsystem.service.impl.TicketServiceImpl;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface TicketService {
    TicketResponse reserveTicket(TicketRequest request);
    List<TicketResponse> getAllTickets();

    @Cacheable(value = TicketServiceImpl.CACHE_NAME, key = "#id")
    TicketResponse getTicketById(Long id);

    @Cacheable(value = TicketServiceImpl.CACHE_NAME, key = "'user_' + #userId")
    List<TicketResponse> getTicketsByUserId(Long userId);

    void cancelTicket(Long ticketId);
}
