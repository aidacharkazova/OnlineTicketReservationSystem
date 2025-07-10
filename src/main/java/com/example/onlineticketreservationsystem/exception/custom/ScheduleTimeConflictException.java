package com.example.onlineticketreservationsystem.exception.custom;

public class ScheduleTimeConflictException extends RuntimeException {
    public ScheduleTimeConflictException(String message) {
        super(message);
    }
}
