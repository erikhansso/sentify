package com.example.sentiment.pojos;

import java.util.Date;

public class DateSentimentScore implements Comparable<DateSentimentScore>{
    private Date date;
    private double avgSentScore;
    private int numberOfTweetsThisDay;

    public DateSentimentScore(Date date, double avgSentScore, int numberOfTweetsThisDay) {
        this.date = date;
        this.avgSentScore = avgSentScore;
        this.numberOfTweetsThisDay = numberOfTweetsThisDay;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAvgSentScore() {
        return avgSentScore;
    }

    public void setAvgSentScore(double avgSentScore) {
        this.avgSentScore = avgSentScore;
    }

    public int getNumberOfTweetsThisDay() {
        return numberOfTweetsThisDay;
    }

    public void setNumberOfTweetsThisDay(int numberOfTweetsThisDay) {
        this.numberOfTweetsThisDay = numberOfTweetsThisDay;
    }

    @Override
    public int compareTo(DateSentimentScore dateSentimentScore) {

            return date.compareTo(dateSentimentScore.date);

    }
}
