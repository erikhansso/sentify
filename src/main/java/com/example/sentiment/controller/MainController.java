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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import twitter4j.TwitterException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ModelAndView getScatterPlot(){
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
        Map<Long, Double> idToSentiment = new HashMap<>();

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
                if(duplicateTweets.isEmpty()){
                    uniqueTweetsNotInDb.add(tweetObject);
                }
            }
            if(!uniqueTweetsNotInDb.isEmpty()) { //Only send unique tweets for sentiment query

                preparedAzureQuery = SentimentQueryBuilder.buildSentimentQueries(uniqueTweetsNotInDb);
                //call Azure API
                azureSentimentResponseData = sentimentCommunication.getSentiment(preparedAzureQuery).stream().collect(Collectors.toList());
                //Setting sentiment score of all new tweets from the Azure results
                uniqueTweetsNotInDb = MappingHelper.mapSentimentResponseToTweets(azureSentimentResponseData, uniqueTweetsNotInDb, idToSentiment);
                //only keep tweets with valid sentiment scores
                newTweetsWithValidSentiment = SentimentValidator.sentimentValidator(uniqueTweetsNotInDb, newTweetsWithValidSentiment);
                //save all unique tweets with valid sentiment score to db
                tweetRepository.saveAll(newTweetsWithValidSentiment);

            }

        //Add lists of tweets from DB and filtered new tweets together
        List<Tweet> allTweets = Stream.concat(newTweetsWithValidSentiment.stream(), matchingTweetsStoredInDb.stream())
                .collect(Collectors.toList());
        if(allTweets.isEmpty()){
            return new SearchResource();
        }

        return new SearchResource(allTweets, Statistics.getAverageSentimentOfTweets(allTweets));

    }

}
