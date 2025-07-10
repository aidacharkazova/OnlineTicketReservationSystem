package com.example.onlineticketreservationsystem.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class VenueResponse {

    private Long id;

    private String name;

    private String address;

    private String type;

    private List<SeatResponse> seats;

    private List<ScheduleResponse> schedules;
}
