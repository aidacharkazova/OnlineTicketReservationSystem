package com.example.onlineticketreservationsystem.dto.response;

import com.example.onlineticketreservationsystem.model.enumeration.TicketStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketResponse {
    private Long id;
    private String seatNumber;
    private TicketStatus status;
    private String username;
    private LocalDateTime bookingTime;
}
