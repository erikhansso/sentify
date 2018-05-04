package com.example.sentiment.apis;

import com.example.sentiment.entities.QueryEntity;
import com.example.sentiment.entities.Tweet;
import com.example.sentiment.utilities.DuplicateTweetFilterer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

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

    public List<Tweet> getTweetsByQuery(String query, QueryEntity queryEntity){

        List<Tweet> tweetObjectList = new ArrayList<>();

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
        q.setLang("sv");
        q.setCount(50); //Number of tweets to be returned, max 100

        Query q2 = new Query(query);
        q2.setLang("en");
        q2.setCount(50);

        QueryResult result = null;
        QueryResult result2 = null;
        try {
            result = twitter.search(q);
            result2 = twitter.search(q2);
        } catch (TwitterException e) {
            System.out.println("The query against Twitter API threw an exception");
            e.printStackTrace();
        }

        List<Status> tweetList = result.getTweets();
        List<Status> tweetList2 = result2.getTweets();

        if (tweetList.size() == 0 && tweetList2.size() == 0)
            return tweetObjectList;

        for (Status status : tweetList) {
            int favoriteCount = (status.isRetweet() ? status.getRetweetedStatus().getFavoriteCount() : status.getFavoriteCount());
            tweetObjectList.add(new Tweet(status.getId(), status.getLang(), status.getText(), status.getUser().getScreenName(), status.getCreatedAt(), queryEntity, status.getRetweetCount(), favoriteCount));
        }

        for (Status status : tweetList2) {
            int favoriteCount = (status.isRetweet() ? status.getRetweetedStatus().getFavoriteCount() : status.getFavoriteCount());
            tweetObjectList.add(new Tweet(status.getId(), status.getLang(), status.getText(), status.getUser().getScreenName(), status.getCreatedAt(), queryEntity, status.getRetweetCount(), favoriteCount));
        }

        tweetObjectList = DuplicateTweetFilterer.duplicateTweetFilterer(tweetObjectList);

        return tweetObjectList;

    }

}
