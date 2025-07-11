package com.example.onlineticketreservationsystem.service.interfaces;

import com.example.onlineticketreservationsystem.dto.request.ScheduleRequest;
import com.example.onlineticketreservationsystem.dto.response.ScheduleResponse;

import java.util.List;

public interface ScheduleService {
    ScheduleResponse createSchedule(ScheduleRequest request);
    List<ScheduleResponse> getAllSchedules();
    ScheduleResponse getScheduleById(Long id);
    void deleteSchedule(Long id);
}
