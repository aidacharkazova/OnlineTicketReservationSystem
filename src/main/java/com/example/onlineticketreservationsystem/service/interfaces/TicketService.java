package com.example.onlineticketreservationsystem.service.interfaces;

import com.example.onlineticketreservationsystem.dto.request.TicketRequest;
import com.example.onlineticketreservationsystem.dto.response.TicketResponse;
import com.example.onlineticketreservationsystem.service.impl.TicketServiceImpl;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface TicketService {
    TicketResponse reserveTicket(TicketRequest request);
    List<TicketResponse> getAllTickets();

    TicketResponse getTicketById(Long id);

    List<TicketResponse> getTicketsByUserId(Long userId);

    void cancelTicket(Long ticketId);

    void deleteFromCache(long id);

}
