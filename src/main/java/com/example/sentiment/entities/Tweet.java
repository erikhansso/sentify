package com.example.sentiment.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String handle;
    private long azureId;

    @Column(length = 500)
    private String tweetText;
    private String language;
    private Date createdAt;
    private double sentimentScore;


    //Foreign key to query-entity
    @ManyToOne
    private QueryEntity query;

    public Tweet() {
    }

    // Constructors
    public Tweet(long azureId, String language, String tweetText, String handle, Date createdAt, QueryEntity query) {
        this.azureId = azureId;
        this.tweetText = tweetText;
        this.language = language;
        this.handle = handle;
        this.createdAt = createdAt;
        this.query = query;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", handle='" + handle + '\'' +
                ", azureId=" + azureId +
                ", language='" + language + '\'' +
                ", createdAt=" + createdAt +
                ", query='" + query + '\'' +
                ", sentimentScore=" + sentimentScore +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public QueryEntity getQueryEntity() {
        return query;
    }

    public void setQueryEntity(QueryEntity query) {
        this.query = query;
    }
}