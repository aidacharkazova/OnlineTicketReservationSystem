package com.example.onlineticketreservationsystem.service.impl;

import com.example.onlineticketreservationsystem.dto.request.TicketRequest;
import com.example.onlineticketreservationsystem.dto.response.TicketResponse;
import com.example.onlineticketreservationsystem.exception.custom.DuplicateTicketException;
import com.example.onlineticketreservationsystem.exception.custom.ResourceNotFoundException;
import com.example.onlineticketreservationsystem.mapper.TicketMapper;
import com.example.onlineticketreservationsystem.model.entity.AppUser;
import com.example.onlineticketreservationsystem.model.entity.Schedule;
import com.example.onlineticketreservationsystem.model.entity.Seat;
import com.example.onlineticketreservationsystem.model.entity.Ticket;
import com.example.onlineticketreservationsystem.model.enumeration.TicketStatus;
import com.example.onlineticketreservationsystem.repository.ScheduleRepository;
import com.example.onlineticketreservationsystem.repository.SeatRepository;
import com.example.onlineticketreservationsystem.repository.TicketRepository;
import com.example.onlineticketreservationsystem.repository.UserRepository;
import com.example.onlineticketreservationsystem.service.interfaces.TicketService;
import com.example.onlineticketreservationsystem.service.interfaces.VenueService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final UserRepository appUserRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private static final String CACHE_NAME = "tickets";
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Override
    public TicketResponse reserveTicket(TicketRequest request) {

        boolean exists = ticketRepository.existsByScheduleIdAndSeatId(request.getScheduleId(), request.getSeatId());
        if (exists) {
            throw new DuplicateTicketException("Seat already booked for this schedule");
        }

        AppUser user = appUserRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setSeat(seat);
        ticket.setSchedule(schedule);
        ticket.setStatus(TicketStatus.RESERVED);
        ticket.setBookingTime(LocalDateTime.now());

        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    @Cacheable(value = CACHE_NAME, key = "'all'")
    @Override
    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toResponse)
                .collect(Collectors.toList());
    }
}
