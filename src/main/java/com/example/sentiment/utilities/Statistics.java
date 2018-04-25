package com.example.sentiment.utilities;

import com.example.sentiment.entities.Tweet;


import java.util.List;

public class Statistics {
    public static double getAverageSentimentOfTweets(List<Tweet> tweets){

        double average;
        double sumOfSentimentScores = 0;

        for (Tweet tweet : tweets) {
            sumOfSentimentScores += tweet.getSentimentScore();
        }

        average = sumOfSentimentScores / tweets.size();

        return average;
    }
}
