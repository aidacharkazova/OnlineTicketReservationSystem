package com.example.onlineticketreservationsystem.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AppUserResponse {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
