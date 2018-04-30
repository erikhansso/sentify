package com.example.sentiment.pojos;

public class TweetAverage {
    private int numberOfTweets;
    private double averageSentimentScore;

    public TweetAverage(int numberOfTweets, double averageSentimentScore) {
        this.numberOfTweets = numberOfTweets;
        this.averageSentimentScore = averageSentimentScore;
    }

    public int getNumberOfTweets() {
        return numberOfTweets;
    }

    public void setNumberOfTweets(int numberOfTweets) {
        this.numberOfTweets = numberOfTweets;
    }

    public double getAverageSentimentScore() {
        return averageSentimentScore;
    }

    public void setAverageSentimentScore(double averageSentimentScore) {
        this.averageSentimentScore = averageSentimentScore;
    }
}
