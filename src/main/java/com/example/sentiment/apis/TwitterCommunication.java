package com.example.sentiment.apis;

import com.example.sentiment.entities.Documents;
import com.example.sentiment.entities.Sentiment;
import com.example.sentiment.entities.SentimentQueryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TwitterCommunication {

    @Value("${consumerKey}")
    private String consumerKey; // The application's consumer key

    @Value("${consumerSecret}")
    private String consumerSecret; // The application's consumer secret

    @Value("${accessToken}")
    private String accessToken; // The access token granted after OAuth authorization

    @Value("${accessTokenSecret}")
    private String accessTokenSecret;// The access token secret granted after OAuth authorization

    public TwitterCommunication() {
    }

    public List<String> getTweetByQuery(String query) throws twitter4j.TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret)
                .setTweetModeExtended(true);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Query q = new Query(query);
        q.setCount(1); //Number of tweets to be returned, max 100
        QueryResult result = twitter.search(q);
        return result.getTweets().stream()
                .map(item -> item.getText())
                .collect(Collectors.toList());


    }


    // TODO: 2018-04-24 Method that returns a List of SentimentQuery objects
//    public List<SentimentQuery> getTweetByQuery(String query) throws twitter4j.TwitterException {
//        ConfigurationBuilder cb = new ConfigurationBuilder();
//        cb.setDebugEnabled(true)
//                .setOAuthConsumerKey(consumerKey)
//                .setOAuthConsumerSecret(consumerSecret)
//                .setOAuthAccessToken(accessToken)
//                .setOAuthAccessTokenSecret(accessTokenSecret)
//                .setTweetModeExtended(true);
//        TwitterFactory tf = new TwitterFactory(cb.build());
//        Twitter twitter = tf.getInstance();
//        Query q = new Query(query);
//        q.setCount(1); //Number of tweets to be returned, max 100
//        QueryResult result = twitter.search(q);
//
//        return result.getTweets().stream()
//                .map(item -> item.getText())
//                .collect(Collectors.toList());
//    }
}
