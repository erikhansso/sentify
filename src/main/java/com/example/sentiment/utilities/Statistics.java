package com.example.sentiment.utilities;

import com.example.sentiment.entities.Tweet;
import org.springframework.stereotype.Component;

import java.util.List;

public class Statistics {

    // Fields
    private List<StatisticsTestObject> objectList;

    // Constructors
    public Statistics(List<StatisticsTestObject> objectList) {
        this.objectList = objectList;
    }

//     Getters & Setters
    public List<StatisticsTestObject> getObjectList() {
        return objectList;
    }

    // Perhaps no need for keyword as parameter? Does the object list only contain objects with same keyword?
    public static double getAverageByKeyWord(String keyword, List<StatisticsTestObject> objectList) {

        double average;
        double totalScore = 0;
        double objectCount = objectList.size();

        for (StatisticsTestObject object : objectList) {
            totalScore += object.getScore();
        }

        average = totalScore / objectCount;

        return average;
    }

    public static double getAverageSentimentOfTweets(List<Tweet> tweets) throws IllegalArgumentException{
        //This method does not check if tweets have a sentiment score of 0.0
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
    public static double getAverageSentimentOfFilteredTweets(List<Tweet> tweets) throws IllegalArgumentException{
        // This method does not count tweets with a sentiment score of 0.0 (errors)
        // or tweets with a sentiment score of 0.5 (not analyzed for sentiment)
        double average = 0;
        double sumOfSentimentScores = 0;
        int numberOfValidTweets = 0;
        if(tweets.size() == 0){
            throw new IllegalArgumentException("List of tweets can't be empty");
        }

        for (Tweet tweet : tweets) {
            if(tweet.getSentimentScore() != 0.0 && tweet.getSentimentScore() != 0.5){
                sumOfSentimentScores += tweet.getSentimentScore();
                numberOfValidTweets++;
            }
        }

        if(numberOfValidTweets > 0){
            average = sumOfSentimentScores / numberOfValidTweets;
        }

        return average;
    }
}
