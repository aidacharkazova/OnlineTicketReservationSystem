package com.example.onlineticketreservationsystem.dto.request;

import com.example.onlineticketreservationsystem.validation.FutureLocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureLocalDateTime
    private LocalDateTime endTime;

}
