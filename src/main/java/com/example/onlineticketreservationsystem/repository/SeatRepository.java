package com.example.onlineticketreservationsystem.repository;


import com.example.onlineticketreservationsystem.model.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
}
