package com.example.onlineticketreservationsystem.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScheduleResponse {
    private String eventName;

    private String venueName;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private List<TicketResponse> tickets;
}
