package com.example.onlineticketreservationsystem.repository;

import com.example.onlineticketreservationsystem.model.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
}
