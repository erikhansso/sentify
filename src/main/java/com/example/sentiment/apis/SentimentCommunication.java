package com.example.sentiment.apis;


import com.example.sentiment.pojos.Documents;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SentimentCommunication {

        @Value("${azureKey}")
        String azureKey;

    public SentimentCommunication() {
    }

    public String getSentiment(Documents docs) {

        String sent = "";

        try {
            sent = GetSentiment.getSentiment(docs, azureKey);
            return sent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sent;
    }

}
