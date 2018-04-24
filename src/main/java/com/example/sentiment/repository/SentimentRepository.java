package com.example.sentiment.repository;


import com.example.sentiment.entities.EntityTest;
import org.springframework.data.repository.CrudRepository;

public interface SentimentRepository extends CrudRepository<EntityTest, Long> {
}
