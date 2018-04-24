package com.example.sentiment.controller;

import com.example.sentiment.apis.SentimentCommunication;
import com.example.sentiment.apis.TwitterCommunication;

import com.example.sentiment.entities.QueryEntity;
import com.example.sentiment.entities.TestTweet;

import com.example.sentiment.entities.Tweet;
import com.example.sentiment.pojos.Documents;
import com.example.sentiment.pojos.Sentiment;
import com.example.sentiment.entities.SentimentQueryBuilder;
import com.example.sentiment.repository.QueryRepository;
import com.example.sentiment.repository.TweetRepository;
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
//    @Autowired
//    TweetRepository tweetRepository;
//    @Autowired
//    QueryRepository queryRepository;


    @GetMapping("/")
    public ModelAndView getStartPage() {
        return new ModelAndView("index");
    }

    @PostMapping("/searchForTweets")
    @ResponseBody
    public List<String> getTweets(@RequestParam String searchInput) {
        List<String> dummystring = new ArrayList<>();
        List<Tweet> tweetObjects;
        Documents sentimentQueryList;
        List<Sentiment> sentimentResponse = new ArrayList<>();

        //checks if query is already in database, commented out because row 79 doesnt work yet




//        if (queryRepository.findByQueryText(searchInput) == null) {
//            Query query = new Query(searchInput);
//            queryRepository.save(query);
//            //Following three lines are just for test purposes, can be removed when method is done
//            List<Tweet> testTweets = new ArrayList<>();
//            testTweets.add(new Tweet("Elin", "Hej", LocalDateTime.now(), 1, query));
//            tweetRepository.saveAll(testTweets);
//        } else {
//            //if the query already exists get all tweets associated with that query
//            Iterable<Tweet> tweetsAlreadyInDatabase = tweetRepository.findByQuery(queryRepository.findByQueryText(searchInput));
//        }

        try {
            //QueryEntity queryEntity = new QueryEntity(searchInput);
            //queryRepository.save(queryEntity);
            tweetObjects = twitterCommunication.getTweetsByQuery(searchInput);
            sentimentQueryList = SentimentQueryBuilder.buildSentimentQueries(tweetObjects);
            sentimentResponse = sentimentCommunication.getSentiment(sentimentQueryList).stream().collect(Collectors.toList());
            for (Tweet tweetObject : tweetObjects) { // TODO: Refactor to more efficient implementation
                for (Sentiment sentiment : sentimentResponse) {
                    if(sentiment.getId().equals(String.valueOf(tweetObject.getAzureId()))){
                        tweetObject.setSentimentScore(Double.parseDouble(sentiment.getScore()));
                        break;
                    }
                }
            }
            for (Tweet tweetObject : tweetObjects) {
                System.out.println(tweetObject.toString());
            }

            // TODO: 2018-04-24 Comment out when we got a list of tweet objects that should be added to the database
            //Methods that will save tweet objects to database
//            tweetRepository.saveAll(tweets);


        } catch (twitter4j.TwitterException e) {
            e.printStackTrace();
            System.out.println("No tweets were found for query: " + searchInput);
            return Arrays.asList("No tweets were found.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong with sentiment query");
        }


        return dummystring;

    }


}
