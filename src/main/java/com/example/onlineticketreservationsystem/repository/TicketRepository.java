package com.example.onlineticketreservationsystem.repository;

import com.example.onlineticketreservationsystem.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
