package com.example.sentiment.utilities;

import com.example.sentiment.entities.Tweet;


import java.util.List;
import java.util.stream.Collectors;

public class Statistics {
    public static double getAverageSentimentOfTweets(List<Tweet> tweets) {

        double average;
        double sumOfSentimentScores = 0;

        for (Tweet tweet : tweets) {
            sumOfSentimentScores += tweet.getSentimentScore();
        }

        average = sumOfSentimentScores / tweets.size();

        return average;
    }

    public static double getStandardDeviation(List<Tweet> tweets) {
        if(tweets.size() == 1)
            return 0.0;
        return Math.sqrt(getVariance(tweets));
    }

    private static double getVariance(List<Tweet> tweets) {
        double mean = getAverageSentimentOfTweets(tweets);
        double sumOfSquaredDifferences = 0.0;
        for (Tweet tweet : tweets) {
            sumOfSquaredDifferences += Math.pow(tweet.getSentimentScore() - mean, 2);
        }
        return sumOfSquaredDifferences / (tweets.size() - 1);
    }

    public static double getMedian(List<Tweet> tweets) {
        List<Double> sentimentScores = tweets.stream()
                .map(Tweet::getSentimentScore)
                .sorted()
                .collect(Collectors.toList());
        int middle = sentimentScores.size() / 2;

        return (sentimentScores.size() % 2 == 0 ? (sentimentScores.get(middle - 1) + sentimentScores.get(middle)) / 2 :
                sentimentScores.get(middle));
    }
}
