package com.example.sentiment.utilities;

import com.example.sentiment.entities.Tweet;
import com.example.sentiment.pojos.Sentiment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentimentMappingHelper {

    public static List<Tweet> mapSentimentResponseToTweets(List<Sentiment> azureSentimentResponseData, List<Tweet> uniqueTweetsNotInDb) {

        Map<Long, Double> idToSentiment = new HashMap<>();

        for (Tweet uniqueTweet : uniqueTweetsNotInDb) {
            idToSentiment.put(uniqueTweet.gettweetId(), 0.0);
        }

        for (Sentiment sentiment : azureSentimentResponseData) {
            idToSentiment.put(Long.parseLong(sentiment.getId()), Double.parseDouble(sentiment.getScore()));
        }

        for (Tweet uniqueTweet : uniqueTweetsNotInDb) {
            uniqueTweet.setSentimentScore(idToSentiment.get(uniqueTweet.gettweetId()));
        }

        return uniqueTweetsNotInDb;
    }

}
