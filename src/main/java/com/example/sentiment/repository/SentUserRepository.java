package com.example.sentiment.repository;

import com.example.sentiment.entities.SentUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentUserRepository extends JpaRepository<SentUser, Long> {
    SentUser findByEmail(String email);
}
