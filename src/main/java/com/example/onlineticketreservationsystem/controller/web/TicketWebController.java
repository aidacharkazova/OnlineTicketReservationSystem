package com.example.onlineticketreservationsystem.controller.web;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TicketWebController {

    @GetMapping("/my-tickets")
    public String myTickets() {
        return "tickets/my_tickets"; // Refers to src/main/resources/templates/tickets/my_tickets.html
    }
}