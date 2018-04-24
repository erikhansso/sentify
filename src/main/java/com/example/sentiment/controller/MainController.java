package com.example.sentiment.controller;

import com.example.sentiment.apis.SentimentCommunication;
import com.example.sentiment.apis.TwitterCommunication;
import com.example.sentiment.pojos.Documents;
import com.example.sentiment.pojos.Sentiment;
import com.example.sentiment.entities.SentimentQueryBuilder;
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
       // String testSentiment = sentimentCommunication.getSentiment(testTweet);

        return new ModelAndView("demo")
                .addObject("tweet", testTweet);
                //.addObject("sentiment", testSentiment);
    }

    @GetMapping("/")
    public ModelAndView getStartPage() {
        return new ModelAndView("index");
    }

    @PostMapping("/searchForTweets")
    @ResponseBody
    public List<String> getTweets(@RequestParam String searchInput) {
        List<String> tweets = new ArrayList<>();
        Documents sentimentQueryList;
        List<Sentiment> sentimentResponse = new ArrayList<>();
        try {
            tweets = twitterCommunication.getTweetByQuery(searchInput);
            sentimentQueryList = SentimentQueryBuilder.buildSentimentQueries(tweets);
            sentimentResponse = sentimentCommunication.getSentiment(sentimentQueryList);
        } catch (twitter4j.TwitterException e) {
            e.printStackTrace();
            System.out.println("No tweets were found for query: " + searchInput);
            return Arrays.asList("No tweets were found.");
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Something went wrong with sentiment query");
        }

        for (Sentiment sentiment : sentimentResponse) {
            System.out.println(sentiment.toString());
        }

        return tweets;
    }



}
