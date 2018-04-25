package com.example.sentiment.apis;

import com.example.sentiment.entities.QueryEntity;
import com.example.sentiment.entities.Tweet;
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

    public List<Tweet> getTweetsByQuery(String query, QueryEntity queryEntity) throws twitter4j.TwitterException {


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
        q.setCount(10); //Number of tweets to be returned, max 100
        QueryResult result = twitter.search(q);
        List<Status> tweetList = result.getTweets();


        Query q2 = new Query(query);
        q2.setLang("en");
        q2.setCount(10);
        QueryResult result2 = twitter.search(q2);
        List<Status> tweetList2 = result2.getTweets();


        if(tweetList.size() == 0 && tweetList2.size() == 0)
            throw new TwitterException("No tweets were found");

        for (Status status : tweetList) {
            tweetObjectList.add(new Tweet(status.getId(), status.getLang(), status.getText(), status.getUser().getScreenName(), status.getCreatedAt(), queryEntity));
        }

        for (Status status : tweetList2) {
            tweetObjectList.add(new Tweet(status.getId(), status.getLang(), status.getText(), status.getUser().getScreenName(), status.getCreatedAt(), queryEntity));
        }

        return tweetObjectList;

    }

}
