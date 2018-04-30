package com.example.sentiment.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Tweet implements Comparable<Tweet>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String handle;
    private long tweetId;

    @Column(length = 500)
    private String tweetText;
    private String language;
    private Date createdAt;
    private double sentimentScore;
    private int favoriteCount;
    private int retweetCount;

    //Foreign key to query-entity
    @ManyToOne
    private QueryEntity query; //this class has to contain a variable named query in order to work for some reason


    public Tweet() {
    }

    // Constructors
    public Tweet(long tweetId, String language, String tweetText, String handle, Date createdAt, QueryEntity query, int retweetCount, int favoriteCount) {
        this.tweetId = tweetId;
        this.tweetText = tweetText;
        this.language = language;
        this.handle = handle;
        this.createdAt = createdAt;
        this.query = query;
        this.favoriteCount = favoriteCount;
        this.retweetCount = retweetCount;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", handle='" + handle + '\'' +
                ", tweetId=" + tweetId +
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

    public long gettweetId() {
        return tweetId;
    }

    public void settweetId(long tweetId) {
        this.tweetId = tweetId;
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

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    @Override
    public int compareTo(Tweet tweet) {
        return createdAt.compareTo(tweet.createdAt);
    }

}