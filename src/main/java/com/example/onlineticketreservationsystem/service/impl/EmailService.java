package com.example.onlineticketreservationsystem.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBookingConfirmation(String toEmail, String eventName, Long ticketId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("🎟️ Your Ticket Confirmation for " + eventName);
        message.setText("Thank you for booking!\n\n" +
                "Event: " + eventName + "\n" +
                "Ticket ID: " + ticketId + "\n\n" +
                "Enjoy your event! 😊\n" +
                "Online Ticket Reservation System");
        mailSender.send(message);
    }
}
