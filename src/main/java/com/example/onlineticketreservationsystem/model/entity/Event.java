package com.example.onlineticketreservationsystem.model.entity;

import com.example.onlineticketreservationsystem.model.enumeration.EventType;
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
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private EventType type; // MOVIE, FLIGHT, etc.

    @OneToMany(mappedBy = "event")
    @JsonIgnore
    private List<Schedule> schedules;
}
