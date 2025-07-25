package com.example.onlineticketreservationsystem.controller;



import com.example.onlineticketreservationsystem.dto.response.AppUserResponse;
import com.example.onlineticketreservationsystem.service.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<AppUserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<AppUserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
