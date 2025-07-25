package com.example.onlineticketreservationsystem.service.impl;

import com.example.onlineticketreservationsystem.dto.request.TicketRequest;
import com.example.onlineticketreservationsystem.dto.response.TicketResponse;
import com.example.onlineticketreservationsystem.exception.custom.ResourceNotFoundException;
import com.example.onlineticketreservationsystem.exception.custom.SeatAlreadyBookedException;
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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
    private final EmailService emailService;

    @Override
    public TicketResponse reserveTicket(TicketRequest request) {

        boolean exists = ticketRepository.existsByScheduleIdAndSeatId(request.getScheduleId(), request.getSeatId());
        if (exists) {
            throw new SeatAlreadyBookedException("Seat already booked for this schedule");
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
        Ticket saved = ticketRepository.save(ticket);

        emailService.sendBookingConfirmation(user.getEmail(), schedule.getEvent().getName(),ticket.getId());
        return ticketMapper.toResponse(saved);

    }

    @Cacheable(value = CACHE_NAME, key = "'all'")
    @Override
    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toResponse)
                .collect(Collectors.toList());
    }
    @Cacheable(value = CACHE_NAME, key = "#id")
    @Override
    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        return ticketMapper.toResponse(ticket);
    }

    @Cacheable(value = CACHE_NAME, key = "'user_' + #userId")
    @Override
    public List<TicketResponse> getTicketsByUserId(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return ticketRepository.findByUser(user).stream()
                .map(ticketMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));
        ticketRepository.delete(ticket);
    }


    @CacheEvict(value = CACHE_NAME, key = "#id")
    public void deleteFromCache(long id) {}

}
