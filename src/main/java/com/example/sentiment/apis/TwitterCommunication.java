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

    String consumerKey = ""; // The application's consumer key
    String consumerSecret = ""; // The application's consumer secret
    String accessToken = ""; // The access token granted after OAuth authorization
    String accessTokenSecret = ""; // The access token secret granted after OAuth authorization

    public void getTweets()throws IOException {
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);

        SearchParameters params = new SearchParameters("#stockholm");
        params.lang("sv").count(1);
        SearchResults results = twitter.searchOperations().search(params);

        List<Tweet> tweets = results.getTweets();

        List<String> lines = new ArrayList<>();
        for (Tweet t : tweets) {
            System.out.println(t.getText());
            lines.add(t.getText());
        }

        //Writes the tweets to a file
        // TODO: 2018-04-23 Write to database instead
        Path file = Paths.get("the-file-name.txt");
        Files.write(file, lines, Charset.forName("UTF-8"));
    }
}
