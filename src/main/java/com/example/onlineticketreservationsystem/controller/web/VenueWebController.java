package com.example.onlineticketreservationsystem.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VenueWebController {

    @GetMapping("/venues/create")
    public String createVenueForm() {
        return "venues/create"; // Refers to src/main/resources/templates/venues/create.html
    }
}