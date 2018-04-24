package com.example.sentiment.controller;

import com.example.sentiment.entities.EntityTest;
import com.example.sentiment.repository.SentimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DBController {

    @Autowired
    SentimentRepository sentimentRepository;

    @GetMapping("/admin")
    public Iterable<EntityTest> getAdmin(){
        return sentimentRepository.findAll();
    }

    @GetMapping("/add/{name}")
    public String add(@PathVariable String name) {
        sentimentRepository.save(new EntityTest(name));
        return "ok";
    }

}
