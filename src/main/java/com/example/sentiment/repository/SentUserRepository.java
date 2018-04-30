package com.example.sentiment.repository;

import com.example.sentiment.entities.QueryEntity;
import com.example.sentiment.entities.SentUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SentUserRepository extends CrudRepository<SentUser, Long> {

    SentUser findByEmail(String email);
}
