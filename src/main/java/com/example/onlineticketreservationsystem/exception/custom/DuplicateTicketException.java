package com.example.onlineticketreservationsystem.exception.custom;

public class DuplicateTicketException extends RuntimeException {
    public DuplicateTicketException(String message) {
        super(message);
    }
}
