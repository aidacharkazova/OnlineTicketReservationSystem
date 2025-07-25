package com.example.onlineticketreservationsystem.dto.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String fullName;
    private String email;
    private String password;
}

