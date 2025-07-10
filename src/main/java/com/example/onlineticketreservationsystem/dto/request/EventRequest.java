package com.example.onlineticketreservationsystem.dto.request;

import com.example.onlineticketreservationsystem.model.enumeration.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventRequest {

    @NotBlank(message = "Event name cannot be empty!")
    private String name;

    private String description;

    @NotNull(message = "Event type is required!")
    private EventType type;

}
