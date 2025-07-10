package com.example.onlineticketreservationsystem.dto.response;

import com.example.onlineticketreservationsystem.model.enumeration.EventType;
import lombok.Data;

import java.util.List;

@Data
public class EventResponse {

    private Long id;

    private String name;

    private String description;

    private EventType type;

    private List<ScheduleResponse> schedules;
}
