package com.example.sentiment.pojos;

import com.example.sentiment.entities.Tweet;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SearchResource {
    private List<Tweet> tweets;
    private double averageSentiment;
    private List<DateSentimentScore> avgSentimentGroupedByDate;
    private double standardDeviation;


    public SearchResource() {
    }

    public SearchResource(List<Tweet> tweets, double averageSentiment) {
        this.tweets = tweets;
        this.averageSentiment = averageSentiment;
    }

    public SearchResource(List<Tweet> tweets, double averageSentiment, List<DateSentimentScore> avgSentimentGroupedByDate) {
        this.tweets = tweets;
        this.averageSentiment = averageSentiment;
        this.avgSentimentGroupedByDate = avgSentimentGroupedByDate;
    }

    public SearchResource(List<Tweet> tweets, double averageSentiment, List<DateSentimentScore> avgSentimentGroupedByDate,
    double standardDeviation){
        this.tweets = tweets;
        this.averageSentiment = averageSentiment;
        this.avgSentimentGroupedByDate = avgSentimentGroupedByDate;
        this.standardDeviation = standardDeviation;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    public double getAverageSentiment() {
        return averageSentiment;
    }

    public void setAverageSentiment(double averageSentiment) {
        this.averageSentiment = averageSentiment;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }
    public List<DateSentimentScore> getAvgSentimentGroupedByDate() {
        return avgSentimentGroupedByDate;
    }

    public void setAvgSentimentGroupedByDate(List<DateSentimentScore> avgSentimentGroupedByDate) {
        this.avgSentimentGroupedByDate = avgSentimentGroupedByDate;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }


    @Override
    public String toString() {
        return "SearchResource " +
                "contains " + tweets.size() +
                " tweets, with an average sentiment score of " + averageSentiment;
    }
}
