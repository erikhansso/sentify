package com.example.sentiment.entities;


import java.time.LocalDateTime;

public class Tweet {

    // Fields
    private long id;
    private String handle;
    private String text;
//    private String azureId;
//    private String tweetText;
//    private String query;
//    private String sentiment;
//    private String language;
    private LocalDateTime date;
    private double sentimentScore;

    // Constructors
    public Tweet(long id, String handle, String text, LocalDateTime date, double sentimentScore) {

        this.id = id;
        this.handle = handle;
        this.text = text;
        this.date = date;
        this.sentimentScore = sentimentScore;
    }

    // Getters & Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return handle;
    }

    public void setUser(String handle) {
        this.handle = handle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }
}
