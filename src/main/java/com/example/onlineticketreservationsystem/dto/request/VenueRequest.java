package com.example.onlineticketreservationsystem.dto.request;

import com.example.onlineticketreservationsystem.model.enumeration.VenueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class VenueRequest {

    @NotBlank(message = "Venue name cannot be empty!")
    private String name;

    @NotBlank(message = "Address cannot be empty!")
    private String address;


    @NotNull(message = "Venue type is required!")
    private VenueType type;


    private List<SeatRequest> seats;
}
