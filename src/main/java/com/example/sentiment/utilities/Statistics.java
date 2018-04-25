package com.example.sentiment.utilities;

import com.example.sentiment.entities.Tweet;


import java.util.List;

public class Statistics {
    public static double getAverageSentimentOfTweets(List<Tweet> tweets) throws IllegalArgumentException{
        //Could return zero if things go wrong
        double average;
        double sumOfSentimentScores = 0;
        if(tweets.size() == 0){
            throw new IllegalArgumentException("List of tweets can't be empty");
        }

        for (Tweet tweet : tweets) {
            sumOfSentimentScores += tweet.getSentimentScore();
        }

        average = sumOfSentimentScores / tweets.size();

        return average;
    }
}
