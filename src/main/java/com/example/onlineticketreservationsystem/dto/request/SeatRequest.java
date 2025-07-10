package com.example.onlineticketreservationsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeatRequest {
    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    private String row;

    @NotNull(message = "Seat type is required")
    private String type;
}
