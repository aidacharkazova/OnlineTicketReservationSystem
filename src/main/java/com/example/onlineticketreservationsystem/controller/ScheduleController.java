package com.example.onlineticketreservationsystem.controller;


import com.example.onlineticketreservationsystem.dto.request.ScheduleRequest;
import com.example.onlineticketreservationsystem.dto.response.ScheduleResponse;
import com.example.onlineticketreservationsystem.service.interfaces.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(request));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> getScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cache/{id}")
    public ResponseEntity<Void> deleteCache(@PathVariable Long id) {
        scheduleService.deleteFromCache(id);
        return ResponseEntity.noContent().build();
    }
}
