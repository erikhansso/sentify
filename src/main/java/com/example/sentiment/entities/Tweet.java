package com.example.sentiment.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String handle; //UserName
    private String text;
    private LocalDateTime date;
    private double sentimentScore;

    public Tweet() {
    }

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

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
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
