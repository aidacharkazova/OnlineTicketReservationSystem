package com.example.onlineticketreservationsystem.model.entity;

import com.example.onlineticketreservationsystem.model.enumeration.SeatType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seats")
public class Seat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "seat_number")
    private String seatNumber; // e.g., A1, B10
    private String row; // Optional

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @OneToMany(mappedBy = "seat")
    @JsonIgnore
    private List<Ticket> tickets;

    @Enumerated(EnumType.STRING)
    private SeatType type; // e.g., REGULAR, VIP
}
