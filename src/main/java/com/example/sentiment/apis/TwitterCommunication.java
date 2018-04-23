package com.example.sentiment.apis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;

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

    public String getTweets() {
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);

        SearchParameters params = new SearchParameters("#stockholm");
        params.lang("sv").count(1);
        SearchResults results = twitter.searchOperations().search(params);

        return results.getTweets().get(0).getText();
    }

    public String getTweetByQuery(String query) {
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);

        SearchParameters params = new SearchParameters(query);
        params.lang("sv").count(1);
        SearchResults results = twitter.searchOperations().search(params);

        return results.getTweets().get(0).getText();
    }
}
