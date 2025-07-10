package com.example.onlineticketreservationsystem.repository;

import com.example.onlineticketreservationsystem.model.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
}
