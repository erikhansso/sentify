package com.example.sentiment.controller;

import com.example.sentiment.apis.SentimentCommunication;
import com.example.sentiment.apis.TwitterCommunication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    TwitterCommunication twitterCommunication;
    @Autowired
    SentimentCommunication sentimentCommunication;

    @GetMapping("/demo")
    public ModelAndView getDemo() {
        String testTweet = "";
        try {
            testTweet = twitterCommunication.getTweetByQuery("#jkflaflakl").get(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No tweets were found.");
            testTweet = "No tweets were found";
        }
        String testSentiment = sentimentCommunication.getSentiment(testTweet);
        return new ModelAndView("demo")
                .addObject("tweet", testTweet)
                .addObject("sentiment", testSentiment);
    }

    @GetMapping("/")
    public ModelAndView getStartPage() {
        return new ModelAndView("index");
    }

    @PostMapping("/searchForTweets")
    @ResponseBody
    public List<String> getTweets(@RequestParam String searchInput) {
        List<String> tweets = new ArrayList<>();
        try {
            tweets = twitterCommunication.getTweetByQuery(searchInput);
        } catch (twitter4j.TwitterException e) {
            e.printStackTrace();
            System.out.println("No tweets were found for query: " + searchInput);
            return Arrays.asList("No tweets were found.");
        }

        // String sentimentScore = sentimentCommunication.getSentiment(tweet);

//        List<String> result = new ArrayList<>();
//        result.add(tweet);
//        result.add(sentimentScore);
        return tweets;
    }
}
