package com.example.onlineticketreservationsystem.dto.response;


import lombok.Data;

@Data
public class SeatResponse {
    private Long id;
    private String seatNumber;
    private String row;
    private String type;
}

