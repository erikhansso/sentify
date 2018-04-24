package com.example.sentiment.pojos;

public class Sentiment {


    private String score;
    private String id;

    public Sentiment() {
    }

    public Sentiment(String score, String id) {
        this.id = id;
        this.score = score;
    }

    @Override
    public String toString() {
        return "Sentiment{" +
                "id='" + id + '\'' +
                ", score='" + score + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
