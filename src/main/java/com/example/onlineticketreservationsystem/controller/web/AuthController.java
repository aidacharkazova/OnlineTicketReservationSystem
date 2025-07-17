package com.example.onlineticketreservationsystem.controller.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "auth/login"; // Refers to src/main/resources/templates/auth/login.html
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register"; // Refers to src/main/resources/templates/auth/register.html
    }
}