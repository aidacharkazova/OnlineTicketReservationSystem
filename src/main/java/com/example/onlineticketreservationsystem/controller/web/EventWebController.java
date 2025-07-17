package com.example.onlineticketreservationsystem.controller.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EventWebController {

    @GetMapping("/") // Home page
    public String listEvents() {
        return "events/list"; // Refers to src/main/resources/templates/events/list.html
    }

    @GetMapping("/events/{id}")
    public String eventDetail(@PathVariable Long id, Model model) {
        model.addAttribute("eventId", id); // Pass the ID to the Thymeleaf template
        return "events/detail"; // Refers to src/main/resources/templates/events/detail.html
    }

    @GetMapping("/events/create")
    public String createEventForm() {
        return "events/create"; // Refers to src/main/resources/templates/events/create.html
    }
}
