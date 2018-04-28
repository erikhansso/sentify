package com.example.sentiment.pojos;

import java.util.Date;

public class DateSentimentScore implements Comparable<DateSentimentScore>{
    private Date date;
    private double avgSentScore;

    public DateSentimentScore(Date date, double avgSentScore) {
        this.date = date;
        this.avgSentScore = avgSentScore;
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

    @Override
    public int compareTo(DateSentimentScore dateSentimentScore) {

            return date.compareTo(dateSentimentScore.date);

    }
}
