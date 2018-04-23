package com.example.sentiment.apis;

import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TwitterCommunication {

    String consumerKey = System.getenv("Twitter_consumerKey"); // The application's consumer key
    String consumerSecret = System.getenv("Twitter_consumerSecret"); // The application's consumer secret
    String accessToken = System.getenv("Twitter_accessToken"); // The access token granted after OAuth authorization
    String accessTokenSecret = System.getenv("Twitter_accessTokenSecret"); // The access token secret granted after OAuth authorization

    public TwitterCommunication() {
    }

    public String getTweets() {
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);

        SearchParameters params = new SearchParameters("#stockholm");
        params.lang("sv").count(1);
        SearchResults results = twitter.searchOperations().search(params);

        return results.getTweets().get(1).getText();
    }
}
