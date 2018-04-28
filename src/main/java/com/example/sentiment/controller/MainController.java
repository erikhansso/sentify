package com.example.sentiment.controller;

import com.example.sentiment.apis.SentimentCommunication;
import com.example.sentiment.apis.TwitterCommunication;

import com.example.sentiment.entities.*;
import com.example.sentiment.pojos.*;
import com.example.sentiment.utilities.*;
import com.example.sentiment.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.*;
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

    @GetMapping("/scatter")
    public ModelAndView getScatterPlot() {
        return new ModelAndView("scatter");
    }

    @PostMapping("/searchForTweets")
    @ResponseBody
    public SearchResource getTweets(@RequestParam String searchInput) {

        List<Tweet> newTweetsFromApiQuery = new ArrayList<>();
        List<Tweet> uniqueTweetsNotInDb = new ArrayList<>();
        List<Tweet> newTweetsWithValidSentiment = new ArrayList<>();
        Documents preparedAzureQuery;
        List<Sentiment> azureSentimentResponseData = new ArrayList<>();
        List<Tweet> matchingTweetsStoredInDb = new ArrayList<>();

        //check if query has been done before and old tweets exist in DB
        if (queryRepository.findByQueryText(searchInput) == null) {
            QueryEntity query = new QueryEntity(searchInput);
            queryRepository.save(query);
        } else {
            matchingTweetsStoredInDb = (List<Tweet>) tweetRepository.findByQuery(queryRepository.findByQueryText(searchInput));
        }
        //call twitter API
        newTweetsFromApiQuery = twitterCommunication.getTweetsByQuery(searchInput, queryRepository.findByQueryText(searchInput));
        //checks for duplicate tweets in DB, only allows unique tweets
        for (Tweet tweetObject : newTweetsFromApiQuery) {
            List<Tweet> duplicateTweets = (List) tweetRepository.findByTweetId(tweetObject.gettweetId());
            if (duplicateTweets.isEmpty()) {
                uniqueTweetsNotInDb.add(tweetObject);
            }
        }
        if (!uniqueTweetsNotInDb.isEmpty()) { //Only send unique tweets for sentiment query

            preparedAzureQuery = SentimentQueryBuilder.buildSentimentQueries(uniqueTweetsNotInDb);
            //call Azure API
            azureSentimentResponseData = sentimentCommunication.getSentiment(preparedAzureQuery).stream().collect(Collectors.toList());
            //Setting sentiment score of all new tweets from the Azure results
            uniqueTweetsNotInDb = SentimentMappingHelper.mapSentimentResponseToTweets(azureSentimentResponseData, uniqueTweetsNotInDb);
            //only keep tweets with valid sentiment scores
            newTweetsWithValidSentiment = SentimentValidator.sentimentValidator(uniqueTweetsNotInDb, newTweetsWithValidSentiment);
            //save all unique tweets with valid sentiment score to db
            tweetRepository.saveAll(newTweetsWithValidSentiment);

        }

        //Add lists of tweets from DB and filtered new tweets together
        List<Tweet> allTweets = Stream.concat(newTweetsWithValidSentiment.stream(), matchingTweetsStoredInDb.stream())
                .collect(Collectors.toList());
        if (allTweets.isEmpty()) {
            return new SearchResource();
        }

        //Group the tweets based on date
        Map<LocalDate, List<Tweet>> tweetsGroupedByLocalDate = allTweets.stream().collect(
                Collectors.groupingBy(tweet -> {
                    Date date = tweet.getCreatedAt();
                    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return localDate;
                }));

        Map<Date, Double> tweetsGroupedByDateWithAvgSentScore = new HashMap<>();

        //Converts back to date object because js wants date object
        tweetsGroupedByLocalDate.forEach((key, value) -> {
            tweetsGroupedByDateWithAvgSentScore.put(Date.from(key.atStartOfDay(ZoneId.systemDefault()).toInstant()), Statistics.getAverageSentimentOfTweets(value));
        });

        //Convert map into DateSentimentScore List

        List<DateSentimentScore> dateSentimentScores = new ArrayList<>();
        tweetsGroupedByDateWithAvgSentScore.forEach((key, value) -> {
            dateSentimentScores.add(new DateSentimentScore(key,value));
        });

        Collections.sort(dateSentimentScores);



        //Sorts the tweets by date with most recent ones at index 0
        Collections.sort(allTweets);
        Collections.reverse(allTweets);

        return new SearchResource(allTweets, Statistics.getAverageSentimentOfTweets(allTweets), dateSentimentScores);

    }

}
