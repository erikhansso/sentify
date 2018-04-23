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
import java.util.List;

@Controller
public class MainController {

    @Autowired
    TwitterCommunication twitterCommunication;
    @Autowired
    SentimentCommunication sentimentCommunication;

    @GetMapping("/demo")
    public ModelAndView getDemo() {
        String testTweet = twitterCommunication.getTweets();
        String testSentiment = sentimentCommunication.getSentiment(testTweet);
        return new ModelAndView("demo")
                .addObject("tweet", testTweet)
                .addObject("sentiment", testSentiment);
    }

    @GetMapping("/")
    public ModelAndView getStartPage(){
        return new ModelAndView("index");
    }

    @PostMapping("/searchForTweets")
    @ResponseBody
    public List<String> getTweets(@RequestParam String searchInput){

        String tweet = twitterCommunication.getTweetByQuery(searchInput);
        String sentimentScore = sentimentCommunication.getSentiment(tweet);

        List<String> result = new ArrayList<>();
        result.add(tweet);
        result.add(sentimentScore);
        return result;
    }
}
