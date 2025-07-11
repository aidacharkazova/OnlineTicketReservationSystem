package com.example.onlineticketreservationsystem.repository;

import com.example.onlineticketreservationsystem.model.entity.AppUser;
import com.example.onlineticketreservationsystem.model.entity.Ticket;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByScheduleIdAndSeatId(@NotNull(message = "Schedule ID is required") Long scheduleId, @NotNull(message = "Seat ID is required") Long seatId);
    List<Ticket> findByUser(AppUser user);

}
