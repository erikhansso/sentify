package com.example.sentiment.controller;

import com.example.sentiment.apis.SentimentCommunication;
import com.example.sentiment.apis.TwitterCommunication;

import com.example.sentiment.entities.*;
import com.example.sentiment.pojos.*;
import com.example.sentiment.utilities.*;
import com.example.sentiment.pojos.SentimentQueryBuilder;
import com.example.sentiment.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class MainController {

    @Autowired
    TwitterCommunication twitterCommunication;
    @Autowired
    SentimentCommunication sentimentCommunication;
    @Autowired
    TweetRepository tweetRepository;
    @Autowired
    QueryRepository queryRepository;


    @GetMapping("/")
    public ModelAndView getStartPage() {
        return new ModelAndView("demo");
    }

    @PostMapping("/searchForTweets")
    @ResponseBody
    public SearchResource getTweets(@RequestParam String searchInput) {
        //TODO: broken up into helper methods

        //Tweets from twitter api
        List<Tweet> newTweets = new ArrayList<>();
        //Tweets that are not already in DB
        List<Tweet> tweetObjectsScrubbed = new ArrayList<>();
        //Tweets with valid sentiment score
        List<Tweet> tweetObjectsSentimentFiltered = new ArrayList<>();
        //Needed by Azure API query
        Documents sentimentQueryList;
        //Response from Azure
        List<Sentiment> sentimentResponse = new ArrayList<>();
        //Tweets matching searchInput already in DB
        List<Tweet> tweetsFromDatabase = new ArrayList<>();

        try {
            //check if query has been done before and old tweets exist in DB
            if (queryRepository.findByQueryText(searchInput) == null) {
                QueryEntity query = new QueryEntity(searchInput);
                queryRepository.save(query);
            } else {
                tweetsFromDatabase = (List<Tweet>) tweetRepository.findByQuery(queryRepository.findByQueryText(searchInput));
            }
            //call twitter API
            newTweets = twitterCommunication.getTweetsByQuery(searchInput, queryRepository.findByQueryText(searchInput));
            sentimentQueryList = SentimentQueryBuilder.buildSentimentQueries(newTweets);
            //call Azure API
            sentimentResponse = sentimentCommunication.getSentiment(sentimentQueryList).stream().collect(Collectors.toList());
            //Setting sentiment score of all new tweets from the Azure results
            for (Tweet tweetObject : newTweets) { // TODO: Refactor to more efficient implementation, maybe hashmap?
                for (Sentiment sentiment : sentimentResponse) {
                    if (sentiment.getId().equals(String.valueOf(tweetObject.gettweetId()))) {
                        tweetObject.setSentimentScore(Double.parseDouble(sentiment.getScore()));
                        break;
                    }
                }
            }
            //checks for duplicate tweets in DB, only allows unique tweets
            for (Tweet tweetObject : newTweets) {
                List<Tweet> duplicateTweets = (List) tweetRepository.findByTweetId(tweetObject.gettweetId());
                if(duplicateTweets.isEmpty()){
                    tweetObjectsScrubbed.add(tweetObject);
                }
            }

            if(tweetObjectsScrubbed.isEmpty()) {
                System.out.println("No unique tweets not in db found for this query");
            }
            //remove tweets with invalid sentiment scores
            for (Tweet tweetObject : tweetObjectsScrubbed) {
                if(tweetObject.getSentimentScore() != 0.5 && tweetObject.getSentimentScore() != 0.0){
                    tweetObjectsSentimentFiltered.add(tweetObject);
                }
            }
            //save all unique tweets with valid sentiment score
            tweetRepository.saveAll(tweetObjectsSentimentFiltered);

        } catch (TwitterException e) {
            e.printStackTrace();
            System.out.println("No tweets were found for query: " + searchInput);
            return new SearchResource();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong with sentiment query");
        }

        //Add lists of tweets from DB and filtered new tweets together
        List<Tweet> allTweets = Stream.concat(tweetObjectsSentimentFiltered.stream(), tweetsFromDatabase.stream())
                .collect(Collectors.toList());
        if(allTweets.isEmpty()){
            return new SearchResource();
        }

        //TODO: try/catch for exception thrown by statistical method
        return new SearchResource(allTweets, Statistics.getAverageSentimentOfTweets(allTweets));

    }


}
