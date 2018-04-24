package com.example.sentiment.controller;

import com.example.sentiment.entities.EntityTest;
import com.example.sentiment.entities.Tweet;
import com.example.sentiment.repository.SentimentRepository;
import com.example.sentiment.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RestController
public class DBController {

    @Autowired
    SentimentRepository sentimentRepository;
    TweetRepository tweetRepository;
    @GetMapping("/admin")
    public Iterable<EntityTest> getAdmin(){
        return sentimentRepository.findAll();
    }

    @GetMapping("/add/{name}")
    public String add(@PathVariable String name) {
        sentimentRepository.save(new EntityTest(name));
        return "ok";
    }
    @GetMapping("/addToDatabase")
    public void addTweetsToDatabase(List<Tweet> tweets){
        tweets.add(new Tweet(1, "Erik", "Blablabla", LocalDateTime.now(), 1));
        for (Tweet tweet : tweets) {
            tweetRepository.save(tweet);
        }
    }

    @GetMapping("/findAll")
    public Iterable<Tweet> findAll(){
        return tweetRepository.findAll();
    }


}
