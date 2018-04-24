package com.example.sentiment.entities;

public class StatisticsTestObject {

    private long id;
    private String keyword;
    private String text;
    private double score;

    public StatisticsTestObject(long id, String text, double score, String keyword) {
        this.id = id;
        this.text = text;
        this.score = score;
        this.keyword = keyword;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
