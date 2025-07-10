package com.example.onlineticketreservationsystem.repository;

import com.example.onlineticketreservationsystem.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}
