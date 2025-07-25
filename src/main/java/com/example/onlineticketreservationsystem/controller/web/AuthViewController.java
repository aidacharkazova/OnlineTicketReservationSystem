package com.example.onlineticketreservationsystem.controller.web;

import com.example.onlineticketreservationsystem.dto.request.SignUpRequest;
import com.example.onlineticketreservationsystem.service.impl.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthViewController {

    private final AuthService authService;

    public AuthViewController(AuthService authService) {
        this.authService = authService;
    }

    // Show the login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login"; // Returns templates/auth/login.html
    }

    // Show the registration page with an empty form object
    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "auth/register"; // Returns templates/auth/register.html
    }

    // Process the registration form submission
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("signUpRequest") SignUpRequest request) {
        // Use the AuthService to register the user
        authService.signUp(request);
        // Redirect to the login page with a success message
        return "redirect:/login?registrationSuccess";
    }
}