package com.example.sentiment.pojos;

import com.example.sentiment.pojos.Sentiment;

import java.util.List;

public class SentimentResponse {

    private List<Sentiment> documents;
    private List<AzureError> errors;

    public SentimentResponse() {
    }

    public SentimentResponse(List<Sentiment> documents, List<AzureError> errors) {
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

    public List<AzureError> getErrors() {
        return errors;
    }

    public void setErrors(List<AzureError> errors) {
        this.errors = errors;
    }
}
