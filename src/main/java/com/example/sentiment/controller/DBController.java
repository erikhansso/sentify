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
import java.util.ArrayList;
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
    public String addTweetsToDatabase(List<Tweet> tweets){
//        List<Tweet> tweets1 = new ArrayList<>();
//        tweets1.add(new Tweet(1, "Erik", "Blablabla", LocalDateTime.now(), 1));
//        for (Tweet tweet : tweets1) {
//            tweetRepository.save(tweet);
//        }
        Tweet tweet = new Tweet(1, "Erik", "Blablabla", LocalDateTime.now(), 1);
        tweetRepository.save(tweet);
        return "ok";
    }

    @GetMapping("/findall")
    public Iterable<Tweet> findAll(){
        return tweetRepository.findAll();
    }


}
