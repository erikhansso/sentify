package com.example.sentiment.apis;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.social.twitter.api.SearchParameters;
//import org.springframework.social.twitter.api.SearchResults;
//import org.springframework.social.twitter.api.Twitter;
//import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TwitterCommunication {

    @Value("${consumerKey}")
    String consumerKey; // The application's consumer key

    @Value("${consumerSecret}")
    String consumerSecret; // The application's consumer secret

    @Value("${accessToken}")
    String accessToken; // The access token granted after OAuth authorization

    @Value("${accessTokenSecret}")
    String accessTokenSecret;// The access token secret granted after OAuth authorization

    public TwitterCommunication() {
    }

//    public String getTweets() {
//        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
//
//        SearchParameters params = new SearchParameters("#stockholm");
//        params.lang("sv").count(1);
//        SearchResults results = twitter.searchOperations().search(params);
//
//        return results.getTweets().get(0).getText();
//    }

//    public String getTweetByQuery(String query) {
//        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
//
//        SearchParameters params = new SearchParameters(query);
//        params.lang("sv").count(1);
//        SearchResults results = twitter.searchOperations().search(params);
//
//        return results.getTweets().get(0).getText();
//    }

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
        q.setCount(50);
        QueryResult result = twitter.search(q);

        return result.getTweets().stream()
                .map(item -> item.getText())
                .collect(Collectors.toList());

    }
}
