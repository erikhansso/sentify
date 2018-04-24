package com.example.sentiment.entities;

import java.time.LocalDateTime;
import java.util.Date;

public class TestTweet {

    private long id;
    private String handle;
    private long azureId;
    private String tweetText;
    private String query;
    private String language;
    private Date createdAt;
    private double sentimentScore;

    public TestTweet() {
    }

    public TestTweet(long azureId, String language, String tweetText, String handle, Date createdAt, String query) {
        this.azureId = azureId;
        this.tweetText = tweetText;
        this.language = language;
        this.handle = handle;
        this.createdAt = createdAt;
        this.query = query;
    }

    @Override
    public String toString() {
        return "TestTweet{" +
                "id=" + id +
                ", handle='" + handle + '\'' +
                ", azureId='" + azureId + '\'' +
                ", query='" + query + '\'' +
                ", language='" + language + '\'' +
                ", date=" + createdAt +
                ", sentimentScore=" + sentimentScore +
                '}';
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public long getAzureId() {
        return azureId;
    }

    public void setAzureId(long azureId) {
        this.azureId = azureId;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    
    public double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }
}
