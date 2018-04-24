package com.example.sentiment.entities;

import java.util.List;

public class SentimentResponse {

    private List<Sentiment> documents;
    private List<String> errors;

    public SentimentResponse() {
    }

    public SentimentResponse(List<Sentiment> documents, List<String> errors) {
        this.documents = documents;
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "SentimentResponse{" +
                "documents=" + documents +
                ", errors=" + errors +
                '}';
    }

    public List<Sentiment> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Sentiment> documents) {
        this.documents = documents;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
