package com.example.onlineticketreservationsystem.controller.web;

import com.example.onlineticketreservationsystem.service.interfaces.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class EventViewController {

    private final EventService eventService;

    public EventViewController(EventService eventService) {
        this.eventService = eventService;
    }

    // Show the home page with all events
    @GetMapping
    public String showHomePage(Model model) {
        model.addAttribute("pageTitle", "Welcome to TicketHub");
        model.addAttribute("events", eventService.getAllEvents());
        return "home"; // Returns templates/home.html
    }

    // Show a dedicated page listing all events
    @GetMapping("/events")
    public String showAllEvents(Model model) {
        model.addAttribute("pageTitle", "All Events");
        model.addAttribute("events", eventService.getAllEvents());
        return "events/list"; // Returns templates/events/list.html
    }

    // Show the details for a single event
    @GetMapping("/events/{id}")
    public String showEventDetails(@PathVariable("id") Long id, Model model) {
        // NOTE: You might need to enhance EventService to fetch associated schedules, venues, etc.
        // For now, we'll just get the event details.
        var event = eventService.getEventById(id);
        model.addAttribute("pageTitle", event.getName());
        model.addAttribute("event", event);
        return "events/details"; // Returns templates/events/details.html
    }
}