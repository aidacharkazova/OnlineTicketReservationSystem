package com.example.onlineticketreservationsystem.exception;


import java.time.LocalDateTime;
import java.util.List;


public class ErrorResponse {
    private String message;
    private LocalDateTime timestamp;
    private int status;
    private List<String> errors;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String message, int status) {
        this();
        this.message = message;
        this.status = status;
    }

    public ErrorResponse(String message, int status, List<String> errors) {
        this(message, status);
        this.errors = errors;
    }

    // Getters and setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
