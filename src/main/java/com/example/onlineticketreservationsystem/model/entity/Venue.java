package com.example.onlineticketreservationsystem.model.entity;

import com.example.onlineticketreservationsystem.model.enumeration.VenueType;
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
@Table(name = "venues")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @Enumerated(EnumType.STRING)
    private VenueType type;

    @OneToMany(mappedBy = "venue")
    @JsonIgnore
    private List<Seat> seats;

    @OneToMany(mappedBy = "venue")
    @JsonIgnore
    private List<Schedule> schedules;

}
