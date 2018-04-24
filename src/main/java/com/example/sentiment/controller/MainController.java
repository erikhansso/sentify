package com.example.sentiment.controller;

import com.example.sentiment.apis.SentimentCommunication;
import com.example.sentiment.apis.TwitterCommunication;
import com.example.sentiment.entities.TestTweet;
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
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    TwitterCommunication twitterCommunication;
    @Autowired
    SentimentCommunication sentimentCommunication;



    @GetMapping("/")
    public ModelAndView getStartPage() {
        return new ModelAndView("index");
    }

    @PostMapping("/searchForTweets")
    @ResponseBody
    public List<String> getTweets(@RequestParam String searchInput) {
        List<String> dummystring = new ArrayList<>();
        List<TestTweet> tweetObjects;
        Documents sentimentQueryList;
        List<Sentiment> sentimentResponse = new ArrayList<>();
        try {
            tweetObjects = twitterCommunication.getTweetsByQuery(searchInput);
            sentimentQueryList = SentimentQueryBuilder.buildSentimentQueries(tweetObjects);
            sentimentResponse = sentimentCommunication.getSentiment(sentimentQueryList).stream().collect(Collectors.toList());
            for (TestTweet tweetObject : tweetObjects) { // TODO: Refactor to more efficient implementation
                for (Sentiment sentiment : sentimentResponse) {
                    if(sentiment.getId().equals(String.valueOf(tweetObject.getAzureId()))){
                        tweetObject.setSentimentScore(Double.parseDouble(sentiment.getScore()));
                        break;
                    }
                }
            }
            for (TestTweet tweetObject : tweetObjects) {
                System.out.println(tweetObject.toString());
            }
        } catch (twitter4j.TwitterException e) {
            e.printStackTrace();
            System.out.println("No tweets were found for query: " + searchInput);
            return Arrays.asList("No tweets were found.");
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Something went wrong with sentiment query");
        }



        return dummystring;
    }
}
