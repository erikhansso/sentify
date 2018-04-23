package com.example.sentiment.apis;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SentimentCommunication {

        @Value("${azureKey}")
        String azureKey;

    public SentimentCommunication() {
    }

    public String getSentiment(String tweet) {

        String sent = "";

        try {
            sent = GetSentiment.getSentiment(tweet, azureKey);
            return sent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sent;
    }

}
