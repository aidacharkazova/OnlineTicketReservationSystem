package com.example.onlineticketreservationsystem.dto.request;

import com.example.onlineticketreservationsystem.validation.FutureLocalDateTime;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleRequest {
    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Venue ID is required")
    private Long venueId;

    @NotNull(message = "Start time is required")
    @FutureLocalDateTime
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @FutureLocalDateTime
    private LocalDateTime endTime;

}
